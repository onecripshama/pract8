package com.example.pract8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import androidx.work.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button bStart, btJustDoIt;
    ImageView imageView;
    Button loadButton;
    OneTimeWorkRequest workRequest1 = new OneTimeWorkRequest.Builder(MyWorker.class)
            .build();

    // Вторая задача
    OneTimeWorkRequest workRequest2 = new OneTimeWorkRequest.Builder(MyWorker.class)
            .build();

    // Третья задача
    OneTimeWorkRequest workRequest3 = new OneTimeWorkRequest.Builder(MyWorker.class)
            .build();


    OneTimeWorkRequest workRequest4 = new OneTimeWorkRequest.Builder(MyWorker.class)
            .setInputData(new Data.Builder().putString("key1", "value1").build())
            .build();

    OneTimeWorkRequest workRequest5 = new OneTimeWorkRequest.Builder(MyWorker.class)
            .setInputData(new Data.Builder().putString("key2", "value2").build())
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bStart = findViewById(R.id.btStart);
        btJustDoIt= findViewById(R.id.btJustDoIt);
        imageView = findViewById(R.id.imageView);
        loadButton = findViewById(R.id.loadButton);
        // Создаем список задач для параллельного выполнения
        List<OneTimeWorkRequest> parallelWorkList = new ArrayList<>();
        parallelWorkList.add(workRequest4);
        parallelWorkList.add(workRequest5);

        bStart = findViewById(R.id.btStart);
        btJustDoIt= findViewById(R.id.btJustDoIt);
        // устанавливаем обработчик на кнопку "Начать "
        btJustDoIt.setOnClickListener(v -> {
            WorkManager.getInstance(getApplicationContext()).enqueue(parallelWorkList);
        });
        // устанавливаем обработчик на кнопку "Начать не в потоке"
        bStart.setOnClickListener(view -> {
            WorkManager.getInstance(getApplicationContext())
                    .beginWith(workRequest1)
                    .then(workRequest2)
                    .then(workRequest3)
                    .enqueue();
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
    }
    private void loadImage() {
        DogApi dogApi = ImageLoader.getRetrofitInstance().create(DogApi.class);
        Call<DogResponse> call = dogApi.getRandomDog();
        call.enqueue(new Callback<DogResponse>() {
            @Override
            public void onResponse(Call<DogResponse> call, Response<DogResponse> response) {
                if (response.isSuccessful()) {
                    DogResponse dogResponse = response.body();
                    String imageUrl = dogResponse.getUrl();
                    Glide.with(MainActivity.this)
                            .load(imageUrl)
                            .into(imageView);
                }
            }

            @Override
            public void onFailure(Call<DogResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}