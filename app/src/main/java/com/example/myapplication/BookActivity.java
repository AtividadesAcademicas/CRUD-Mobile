package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookActivity extends AppCompatActivity {
    private static final String Url = "https://serverbibliotroca-production.up.railway.app/api/v1/bibliotroca";
    private static final String LIVRO = "LIVRO";
    List<Book> list = new ArrayList<>();
    OkHttpClient httpClient = new OkHttpClient();
    Gson gson = new Gson();

    EditText txtRegistry;
    EditText txtTitle;
    EditText txtAuthor;
    EditText txtField;
    EditText txtLanguage;
    EditText txtEdition;
    EditText txtDescription;
    EditText txtPublishingCompany;
    EditText txtState;
    EditText txtUserEmail;
    Button btSave;
    Button btSearch;
    Button btUpdate;
    Button btDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);
        initializeFields();
        loadAPI();
        btSave.setOnClickListener( e -> saveBook());
        btSearch.setOnClickListener( e -> searchBook());
        btUpdate.setOnClickListener( e -> updateBook());
        btDelete.setOnClickListener( e -> deleteBook());
    }
    private void initializeFields() {
        txtRegistry = findViewById(R.id.txtRegistry);
        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtField = findViewById(R.id.txtField);
        txtLanguage = findViewById(R.id.txtLanguage);
        txtEdition = findViewById(R.id.txtEdition);
        txtDescription = findViewById(R.id.txtDescription);
        txtPublishingCompany = findViewById(R.id.txtPublishingCompany);
        txtState = findViewById(R.id.txtState);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        btSave = findViewById(R.id.btSave);
        btSearch = findViewById(R.id.btSearch);
        btUpdate = findViewById(R.id.btUpdate);
        btDelete = findViewById(R.id.btDelete);
    }

    private void populate(Book book) {
        if (book != null) {
            txtTitle.setText(book.getTitle());
            txtAuthor.setText(book.getAuthor());
            txtField.setText(book.getField());
            txtLanguage.setText(book.getLanguage());
            txtEdition.setText(book.getEdition());
            txtDescription.setText(book.getDescription());
            txtPublishingCompany.setText(book.getPublishingCompany());
            txtState.setText(book.getState());
            txtUserEmail.setText(book.getUserEmail());
        } else {
            Log.e("BookActivity", "Objeto livro vazio");
        }
    }

    private void saveBook() {
        Book book = new Book();
        book.setTitle(txtTitle.getText().toString());
        book.setAuthor(txtAuthor.getText().toString());
        book.setField(txtField.getText().toString());
        book.setLanguage(txtLanguage.getText().toString());
        book.setEdition(txtEdition.getText().toString());
        book.setDescription(txtDescription.getText().toString());
        book.setPublishingCompany(txtPublishingCompany.getText().toString());
        book.setState(txtState.getText().toString());
        book.setUserEmail(txtUserEmail.getText().toString());
        list.add(book);
        saveAPI(book);
    }

    private void updateBook() {
        Book book = new Book();
        book.setId(txtRegistry.getText().toString());
        book.setTitle(txtTitle.getText().toString());
        book.setAuthor(txtAuthor.getText().toString());
        book.setField(txtField.getText().toString());
        book.setLanguage(txtLanguage.getText().toString());
        book.setEdition(txtEdition.getText().toString());
        book.setDescription(txtDescription.getText().toString());
        book.setPublishingCompany(txtPublishingCompany.getText().toString());
        book.setState(txtState.getText().toString());
        book.setUserEmail(txtUserEmail.getText().toString());
        updateAPI(book);
    }

    private void deleteBook() {
        deleteAPI(Long.parseLong(txtRegistry.getText().toString()));
        txtTitle.setText("");
        txtAuthor.setText("");
        txtField.setText("");
        txtLanguage.setText("");
        txtEdition.setText("");
        txtDescription.setText("");
        txtPublishingCompany.setText("");
        txtState.setText("");
        txtUserEmail.setText("");
    }

    private void searchBook() {
        for(int i = 0; i < list.size(); i++ ) {
            Book book = list.get(i);
            if (book.getRegistry().toString().contains(txtRegistry.getText().toString())) {
                populate(book);
            }
        }
    }

    public void saveAPI(Book book) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Log.i(LIVRO, "Excutando request POST");
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();
            String bookJson = gson.toJson(book);
            Log.i(LIVRO, "JSON Body: " + bookJson);
            RequestBody body = RequestBody.create(bookJson,
                    MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .post(body)
                    .url(Url + "/livros")
                    .build();
            Call call = client.newCall(request);
            Log.i(LIVRO, "Resquest POST feito no servidor");
            try {
                Response response = call.execute();
            } catch (IOException e) {
                Log.e(LIVRO, "Erro", e);
                throw new RuntimeException(e);
            }
        });

    }

    private void loadAPI() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            list.clear();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Url + "/livros")
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String resposta = response.body().string();

                if (resposta != null && !resposta.isEmpty()) {
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(resposta);

                    if (element != null && element.isJsonArray()) {
                        JsonArray jsonArray = element.getAsJsonArray();

                        for (JsonElement jsonElement : jsonArray) {
                            if (jsonElement != null && !jsonElement.isJsonNull() && jsonElement.isJsonObject()) {
                                BookDTO bookDTO = gson.fromJson(jsonElement, BookDTO.class);
                                list.add(BookDTO.returnBook(bookDTO));
                            }
                        }
                        Log.e(LIVRO, "Resquest GET feito no servidor");
                        Log.e(LIVRO, "Resposta: " + resposta);
                    } else {
                        Log.e(LIVRO, "Resposta do servidor não é um array JSON válido.");
                    }
                } else {
                    Log.e(LIVRO, "Resposta do servidor vazia ou nula.");
                }
            } catch (IOException e) {
                Log.e(LIVRO, "Erro: ", e);
                throw new RuntimeException(e);
            }
        });
    }

    public void updateAPI(Book book) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Log.i(LIVRO, "Excutando request PUT");
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();
            String bookJson = gson.toJson(book);
            Log.i(LIVRO, "JSON Body: " + bookJson);
            RequestBody body = RequestBody.create(bookJson,
                    MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .put(body)
                    .url(Url + "/livros/" + book.getId())
                    .build();
            Call call = client.newCall(request);
            Log.i(LIVRO, "Resquest PUT feito no servidor");
            try {
                Response response = call.execute();
            } catch (IOException e) {
                Log.e(LIVRO, "Erro", e);
                throw new RuntimeException(e);
            }
        });

    }

    public void deleteAPI(Long registry) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Log.i(LIVRO, "Excutando request DELETE");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .delete()
                    .url(Url + "/livros/" + registry)
                    .build();
            Call call = client.newCall(request);
            Log.i(LIVRO, "Resquest DELETE feito no servidor");
            try {
                Response response = call.execute();
            } catch (IOException e) {
                Log.e(LIVRO, "Erro", e);
                throw new RuntimeException(e);
            }
        });

    }
}
