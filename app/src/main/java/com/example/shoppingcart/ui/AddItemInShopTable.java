package com.example.shoppingcart.ui;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;

import com.example.shoppingcart.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AddItemInShopTable {

    public static void addRow(final Context context, final TableLayout addalTableLayout, final DatabaseReference databaseReference){
        final TableRow tr= new TableRow(context);

        TableLayout.LayoutParams paramsForRow = new TableLayout.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT,1f);
        paramsForRow.setMargins(10,10,0,20);
        tr.setLayoutParams(paramsForRow);


        final TableRow.LayoutParams paramsForCategoryandProducts = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1.4f);
        final TableRow.LayoutParams paramsForQuantity = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,0.9f);
        final TableRow.LayoutParams paramsForUnit = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1.4f);

        final Spinner catspinner = new Spinner(context); //spin category
        catspinner.setLayoutParams(paramsForCategoryandProducts);

        final Spinner productspinner = new Spinner(context); //spin product
        catspinner.setLayoutParams(paramsForCategoryandProducts);

        final EditText quantity = new EditText(context); //edit qty
        quantity.setLayoutParams(paramsForQuantity);
        quantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        final Spinner unitspinner = new Spinner(context); //spin product
        catspinner.setLayoutParams(paramsForUnit);

        //Other option to create new product
        //new cat
        final EditText etNewCategory = new EditText(context);
        etNewCategory.setLayoutParams(paramsForCategoryandProducts);
        etNewCategory.setHint("New Category");
        //new product
        final EditText etNewProduct = new EditText(context);
        etNewProduct.setLayoutParams(paramsForCategoryandProducts);
        etNewProduct.setHint("New Product");

        //minus button for deletion
        final TableRow.LayoutParams rowMinus = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,.5f);
        ImageButton minusButton = new ImageButton(context);
        minusButton.setImageResource(R.drawable.ic_baseline_indeterminate_check_box_24);
        minusButton.setLayoutParams(rowMinus);

        fetchUnitInSpinner(databaseReference,context,unitspinner);
        fetchCategoryandProduct(databaseReference,context,catspinner,productspinner,tr,etNewCategory,etNewProduct);
        addOrRemoveProductSpinner(productspinner, tr , etNewProduct);

        tr.addView(catspinner);
        tr.addView(unitspinner);
        tr.addView(quantity);
        tr.addView(minusButton);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addalTableLayout.removeView((ViewGroup)view.getParent());
            }
        });

        addalTableLayout.addView(tr, addalTableLayout.getChildCount() - 2, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
    }

    private static void addOrRemoveProductSpinner(final Spinner productspinner, final TableRow tr, final EditText etNewProduct) {
        productspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(productspinner.getSelectedItem().equals("Others")){
                    tr.addView(etNewProduct,2);
                } else {
                    tr.removeView(etNewProduct);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private static void fetchCategoryandProduct(final DatabaseReference databaseReference, final Context context, final Spinner catspinner, final Spinner productspinner, final TableRow tr, final EditText etNewCategory, final EditText etNewProduct) {
        fetchCategoriesInSpinner(databaseReference,context,catspinner);
        catspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectItem = (String)catspinner.getSelectedItem();
                if(selectItem.equals("Others")){
                    tr.removeView(productspinner);
                    tr.addView(etNewCategory,1);
                    tr.addView(etNewProduct,2);
                }
                else {
                    tr.removeView(etNewCategory);
                    if(!tr.getChildAt(1).equals(productspinner)){
                        tr.addView(productspinner,1);
                    }
                    databaseReference.child("Categories").child(selectItem)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final ArrayList<String> productList = new ArrayList<>();

                                    for(DataSnapshot areaSnapShot: dataSnapshot.getChildren()){ //contains data from a Firebase Database location
                                        productList.add(areaSnapShot.getValue(String.class));
                                    }

                                    Collections.sort(productList);
                                    productList.add("Others");

                                    final ArrayAdapter<String> productAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, productList); //store the units in an array
                                    productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Specify the layout to use when the list of choices appears
                                    productspinner.setAdapter(productAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException());

                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private static void fetchCategoriesInSpinner(DatabaseReference databaseReference, final Context context, final Spinner catspinner) {
        databaseReference.child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> catList = new ArrayList<>();
                for(DataSnapshot areaSnapShot: dataSnapshot.getChildren()){ //contains data from a Firebase Database location
                    catList.add(areaSnapShot.getKey());
                }

                Collections.sort(catList);
                catList.add("Others");

                final ArrayAdapter<String> catAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, catList); //store the units in an array
                catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Specify the layout to use when the list of choices appears
                catspinner.setAdapter(catAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private static void fetchUnitInSpinner(DatabaseReference databaseReference, final Context context, final Spinner unitspinner) {
        databaseReference.child("Unit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> unitList = new ArrayList<>();
                for(DataSnapshot areaSnapShot: dataSnapshot.getChildren()){ //contains data from a Firebase Database location
                    unitList.add(areaSnapShot.getValue(String.class));
                }

                Collections.sort(unitList);

                final ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, unitList); //store the units in an array
                unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Specify the layout to use when the list of choices appears
                unitspinner.setAdapter(unitAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); //used to receive events about data changes at a location
    }

}
