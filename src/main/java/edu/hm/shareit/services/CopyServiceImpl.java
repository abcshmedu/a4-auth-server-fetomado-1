package edu.hm.shareit.services;

import edu.hm.shareit.models.Copy;
import edu.hm.shareit.models.Medium;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This is the implementation of the CopyService interface. It does the work
 * behind the /copies/ resource.
 */
public class CopyServiceImpl implements CopyService {

    private static Map<Integer, Copy> copies;

    /**
     * Constructs a new instance.
     */
    public CopyServiceImpl() {
        if (copies == null) {
            copies = new HashMap<>();
        }
    }

    @Override
    public ServiceResult addCopy(String owner, String medium) {
        MediaServiceImpl tmpMsr = new MediaServiceImpl();
        Copy copy;
        Medium mediumToCopy = null;
        String regexISBN = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$";
        String regexBarcode = "^[1-9][0-9]{8,14}$";
        Pattern patternISBN = Pattern.compile(regexISBN);
        Pattern patternBarcode = Pattern.compile(regexBarcode);
        if (patternISBN.matcher(medium).matches()) {
            mediumToCopy = tmpMsr.getBook(medium);
        } else if (patternBarcode.matcher(medium).matches()) {
            mediumToCopy = tmpMsr.getDisc(medium);
        }
        if (mediumToCopy == null) {
            return ServiceResult.NOT_FOUND;
        }
        copy = new Copy(owner, mediumToCopy);
        copies.put(copy.getId(), copy);
        return ServiceResult.OK;
    }

    @Override
    public Copy[] getCopies() {
        return copies.values().toArray(new Copy[copies.size()]);
    }

    @Override
    public Copy getCopy(int id) {
        return copies.get(id);
    }

    @Override
    public ServiceResult updateCopy(int id, String owner) {
        if (!copies.containsKey(id)) {
            return ServiceResult.NOT_FOUND;
        }
        if ((owner == null || owner.equals(""))) {
            return ServiceResult.BAD_REQUEST;
        }
        Copy copyToEdit = copies.get(id);
        if (owner != null && !owner.equals("")) {
            copyToEdit.setOwner(owner);
        }
        return ServiceResult.OK;
    }

    /**
     * Removes the data stored in this service. This method should mainly be used in tests.
     */
    void flushDataForTesting() {
        copies = new HashMap<>();
    }
}
