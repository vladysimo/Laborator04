package ro.pub.cs.systems.eim.lab04.contactsmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    private Button additional_fields_button, save_button, cancel_button;
    private LinearLayout additional_fields_linear_layout;
    private EditText name_text_field, number_text_field, email_text_field, address_text_field;
    private EditText job_title_text_field, company_text_field, website_text_field, im_text_field;

    class MyButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch(v.getId()) {
                case R.id.additional_fields_button:
                    if (additional_fields_linear_layout.getVisibility() == View.GONE) {
                        additional_fields_button.setText(R.string.hide_additional_fields);
                        additional_fields_linear_layout.setVisibility(View.VISIBLE);
                    }
                    else {
                        additional_fields_button.setText(R.string.show_additional_fields);
                        additional_fields_linear_layout.setVisibility(View.GONE);
                    }
                    break;
                case R.id.save_button:

                    String name, phone, email, address, jobTitle, company, website, im;

                    name = name_text_field.getText().toString();
                    phone = number_text_field.getText().toString();
                    email = email_text_field.getText().toString();
                    address = address_text_field.getText().toString();
                    jobTitle = job_title_text_field.getText().toString();
                    company = company_text_field.getText().toString();
                    website = website_text_field.getText().toString();
                    im = website_text_field.getText().toString();

                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                    if (name != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    }
                    if (phone != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                    }
                    if (email != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                    }
                    if (address != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                    }
                    if (jobTitle != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                    }
                    if (company != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                    }
                    ArrayList<ContentValues> contactData = new ArrayList<>();
                    if (website != null) {
                        ContentValues websiteRow = new ContentValues();
                        websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                        websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                        contactData.add(websiteRow);
                    }
                    if (im != null) {
                        ContentValues imRow = new ContentValues();
                        imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                        imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                        contactData.add(imRow);
                    }
                    intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
                    startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
                    break;
                case R.id.cancel_button:
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    finish();
                    break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        additional_fields_button = (Button)findViewById(R.id.additional_fields_button);
        save_button = (Button)findViewById(R.id.save_button);
        cancel_button = (Button)findViewById(R.id.cancel_button);
        additional_fields_linear_layout = (LinearLayout)findViewById(R.id.additional_fields_linear_layout);

        View.OnClickListener buttonListener = new MyButtonListener();
        additional_fields_button.setOnClickListener(buttonListener);
        save_button.setOnClickListener(buttonListener);
        cancel_button.setOnClickListener(buttonListener);

        /**
         * HUGE NAMING CONVENTION ERROR HERE
         * THESE ARE NOT TextFields, THEY ARE EditTexts
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */
        name_text_field = (EditText)findViewById(R.id.name_text_field);
        number_text_field = (EditText)findViewById(R.id.number_text_field);
        email_text_field = (EditText)findViewById(R.id.email_text_field);
        address_text_field = (EditText)findViewById(R.id.address_text_field);
        job_title_text_field = (EditText)findViewById(R.id.job_title_text_field);
        company_text_field = (EditText)findViewById(R.id.company_text_field);
        website_text_field = (EditText)findViewById(R.id.website_text_field);
        im_text_field = (EditText)findViewById(R.id.im_text_field);

        Intent intentFromParent = getIntent();
        String phoneNumber = intentFromParent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
        number_text_field.setText(phoneNumber, TextView.BufferType.EDITABLE);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                number_text_field.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }
}
