package com.miv_sher.testcontacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.viethoa.models.AlphabetItem;

/**
 * Created by Nessa on 12.03.2018.
 */

public class DataHelper {
    private static final String CONTACT_ID = ContactsContract.Contacts._ID;
    private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

    public static void getContacts(Context context, List<Contact> contacts) {
        ContentResolver cr = context.getContentResolver();

        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{PHONE_NUMBER, PHONE_CONTACT_ID},
                null,
                null,
                null
        );
        if (pCur != null) {
            if (pCur.getCount() > 0) {
                HashMap<Integer, ArrayList<String>> phones = new HashMap<>();
                while (pCur.moveToNext()) {
                    Integer contactId = pCur.getInt(pCur.getColumnIndex(PHONE_CONTACT_ID));
                    ArrayList<String> curPhones = new ArrayList<>();
                    if (phones.containsKey(contactId)) {
                        curPhones = phones.get(contactId);
                    }
                    curPhones.add(pCur.getString(0));
                    phones.put(contactId, curPhones);
                }
                Cursor cur = cr.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER},
                        HAS_PHONE_NUMBER + " > 0",
                        null,
                        DISPLAY_NAME + " ASC");
                if (cur != null) {
                    if (cur.getCount() > 0) {
                        //ArrayList<Contact> contacts = new ArrayList<>();
                        while (cur.moveToNext()) {
                            int id = cur.getInt(cur.getColumnIndex(CONTACT_ID));
                            if (phones.containsKey(id)) {
                                Contact con = new Contact();
                                con.setId(id);
                                con.setName(cur.getString(cur.getColumnIndex(DISPLAY_NAME)));
                                con.setPhone(TextUtils.join(",", phones.get(id).toArray()));

                                Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
                                con.setPhoto(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY));

                                contacts.add(con);

                            }
                        }

                    }
                    cur.close();
                }
            }
            pCur.close();
        }
    }

    public static void getAlphabet(List<Contact> contacts, List<AlphabetItem> mAlphabetItems) {
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < contacts.size(); i++) {
            String name = contacts.get(i).getName();
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
    }

}
