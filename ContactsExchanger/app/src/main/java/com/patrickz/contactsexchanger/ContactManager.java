package com.patrickz.contactsexchanger;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ContactManager
{
    private ContentResolver contentResolver;

    public JSONArray contacts = null;

    ContactManager(Context context)
    {
        this.contentResolver = context.getContentResolver();
    }

    private void addContact()
    {
        ArrayList<ContentProviderOperation> op_list = new ArrayList<>();

        op_list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            //.withValue(RawContacts.AGGREGATION_MODE, RawContacts.AGGREGATION_MODE_DEFAULT)
            .build());

        // first and last names
        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "Fartface1")
            //.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, "")
            .build());

        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "123456789")
            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,  ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            .build());

        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Email.DATA, "test@kappa-mm.de")
            .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
            .build());

        try
        {
            ContentProviderResult[] results = contentResolver.applyBatch(ContactsContract.AUTHORITY, op_list);
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }

    private JSONArray getPhoneNumbers(Cursor cursor, String id)
    {
        // if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) <= 0) return null;

        Cursor pCur = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            new String[]{ id },
            null);

        if (pCur == null) return null;

        JSONArray numbers = new JSONArray();

        while (pCur.moveToNext())
        {
            String phoneNo = pCur.getString(pCur.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER));

            String nickName = pCur.getString(pCur.getColumnIndex(
                ContactsContract.CommonDataKinds.Nickname.NAME));

            String NORMALIZED_NUMBER = pCur.getString(pCur.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));

            JSONObject json = new JSONObject();

            json.put("phoneNo", phoneNo);
            json.put("nickName", nickName);
            json.put("normalizedNumber", NORMALIZED_NUMBER);

            numbers.put(json);
        }

        pCur.close();

        if (numbers.length() <= 0) return null;

        return numbers;
    }

    private JSONArray getAdress(String id)
    {
        Uri postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
        Cursor postal_cursor  = contentResolver.query(postal_uri, null,  ContactsContract.Data.CONTACT_ID + "=" + id, null,null);

        if (postal_cursor == null) return null;

        JSONArray adresses = new JSONArray();

        while(postal_cursor.moveToNext())
        {
            String strt  = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
            String cty   = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            String cntry = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));

            JSONObject adr = new JSONObject();
            adr.put("street", strt);
            adr.put("city", cty);
            adr.put("country", cntry);

            adresses.put(adr);
        }

        postal_cursor.close();

        if (adresses.length() <= 0) return null;

        return adresses;
    }

    private JSONArray getEMail(String id)
    {
        Cursor emailCur = contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
            new String[]{id}, null);

        if (emailCur == null) return null;

        JSONArray mail = new JSONArray();

        while (emailCur.moveToNext())
        {
            String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            mail.put(email);
        }

        // Log.d(LOGTAG, json.toString(2));

        emailCur.close();

        if (mail.length() <= 0) return null;

        return mail;
    }

    private JSONObject getContact(Cursor cursor)
    {
        String id   = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//        String nickName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));

//        Log.d(LOGTAG, "id:       " + id);
//        Log.d(LOGTAG, "name:     " + name);
//        Log.d(LOGTAG, "nickName: " + nickName);

        JSONArray numbers  = getPhoneNumbers(cursor, id);
        JSONArray adresses = getAdress(id);
        JSONArray mails    = getEMail(id);

//        if (numbers  != null && numbers.length()  > 0) Log.d(LOGTAG, "numbers:  " + numbers.toString(2));
//        if (adresses != null && adresses.length() > 0) Log.d(LOGTAG, "adresses: " + adresses.toString(2));
//        if (mails    != null && mails.length()    > 0) Log.d(LOGTAG, "mails:    " + mails.toString(2));

        JSONObject contact = new JSONObject();

        // contact.put("id", id);
        contact.put("name", name);
        contact.put("mails", mails);
        contact.put("numbers", numbers);
        contact.put("adresses", adresses);

        // Log.d(LOGTAG, contact.toString(2));

        return contact;
    }

    public void getAllContacts()
    {
        Cursor cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

//        Cursor cursor = contentResolver.query(
//            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//            null,
//            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//            new String[]{ id },
//            null);

        if (cursor == null) return;

        JSONArray tmpContacts = new JSONArray();

        while (cursor.moveToNext())
        {
            tmpContacts.put(getContact(cursor));
        }

        contacts = tmpContacts;

         // Log.d(LOGTAG, contacts.toString(2));

        // addContact();

        cursor.close();
    }

    public void main()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                getAllContacts();
            }
        }).start();
    }
}
