package com.quy.buvape.filter;

import android.util.Log;
import android.widget.Filter;

import com.quy.buvape.adapter.ListContactAdapter;
import com.quy.buvape.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class SearchContact extends Filter {
    private List<Contact> listContactOriginal;
    private ListContactAdapter adapter;

    public SearchContact(List<Contact> listContactOriginal,ListContactAdapter adapter){
        this.listContactOriginal = listContactOriginal;
        this.adapter = adapter;
    }


    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        List<Contact> listContactResult;
        if (charSequence.length() == 0){
            listContactResult = listContactOriginal;
        }else{
            listContactResult = getFilterResult(charSequence.toString().toLowerCase());
        }

        FilterResults results = new FilterResults();
        results.count = listContactResult.size();
        results.values = listContactResult;
        return results;
    }

    private List<Contact> getFilterResult(String value) {
        List<Contact> result = new ArrayList<>();
        for (Contact ct : listContactOriginal){
            if (ct.getName().toLowerCase().contains(value) || ct.getPhone().toLowerCase().contains(value)){
                result.add(ct);
            }
        }
        return result;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.arr = (ArrayList<Contact>) filterResults.values;
        for (Contact n : (ArrayList<Contact>) filterResults.values){
            Log.d("VALUE",n.getName());
        }
        adapter.notifyDataSetChanged();
    }
}
