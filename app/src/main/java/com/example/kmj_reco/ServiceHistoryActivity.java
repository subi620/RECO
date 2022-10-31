package com.example.kmj_reco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.kmj_reco.DTO.ServiceAccount;
import com.example.kmj_reco.utils.ServiceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceHistoryActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    ListView serviceAccountListView;

    static List<ServiceAccount> serviceAccountList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_history);

        /*
        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });*/

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("Service").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceAccountList.clear();

                for(DataSnapshot data : snapshot.getChildren()){
                    ServiceAccount Service = data.getValue(ServiceAccount.class);
                    serviceAccountList.add(Service);
                }
                final ServiceAdapter serviceAdapter = new ServiceAdapter(getApplicationContext(),R.layout.activity_item_service, serviceAccountList, serviceAccountListView);
                serviceAccountListView.setAdapter(serviceAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 더미 데이터
        if (serviceAccountList.size()<=0) {}
        serviceAccountListView = (ListView) findViewById(R.id.loginAdminListView);

        setUpOnClickListener();
    }

    // 아이템 클릭 시 ServiceDetailActivity 화면에 각 데이터 전송
    private void setUpOnClickListener() {
        serviceAccountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ServiceAccount selectedService = (ServiceAccount) serviceAccountListView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), ServiceDetailActivity.class);
                showDetail.putExtra("num", selectedService.getService_Num());
                showDetail.putExtra("title", selectedService.getService_Title());
                showDetail.putExtra("date", selectedService.getService_Date());
                showDetail.putExtra("contents", selectedService.getService_Contents());
                startActivity(showDetail);
            }
        });
    }
}
