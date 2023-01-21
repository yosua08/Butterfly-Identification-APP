package com.example.b_fly_identification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.b_fly_identification.ml.Model;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button camera, gallery;
    ImageView imageView;
    TextView classified;
    TextView result;
    TextView confScore;
    TextView scientific;
    TextView scientificName;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);

        classified = findViewById(R.id.classified);
        result = findViewById(R.id.result);
        confScore = findViewById(R.id.confScore);
        imageView = findViewById(R.id.imageView);
        scientific = findViewById(R.id.scientific);
        scientificName = findViewById(R.id.scientificName);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 3);
                    } else{
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    }
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
    }

    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorImage images = TensorImage.fromBitmap(image);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(images);
            List<Category> scores1 = outputs.getScoresAsCategoryList();
            TensorBuffer scores = outputs.getScoresAsTensorBuffer();

            float[] confidences = scores.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"AMERICAN SNOOT", "GREEN CELLED CATTLEHEART", "MANGROVE SKIPPER", "ORCHARD SWALLOW", "CLODIUS PARNASSIAN", "RED SPOTTED PURPLE", "ORANGE TIP", "CRIMSON PATCH", "COMMON BANDED AWL",
                    "VICEROY", "AN 88", "IPHICLUS SISTER", "PAINTED LADY", "TROPICAL LEAFWING", "MOURNING CLOAK", "SCARCE SWALLOW", "EASTERN PINE ELFIN", "PINE WHITE", "MILBERTS TORTOISESHELL", "PURPLISH COPPER",
                    "QUESTION MARK", "PAPER KITE", "TWO BARRED FLASHER", "RED POSTMAN", "GOLD BANDED", "COMMON WOOD-NYMPH", "APPOLLO", "BROWN SIPROETA", "MALACHITE", "YELLOW SWALLOW TAIL", "PIPEVINE SWALLOW",
                    "CRECENT", "DANAID EGGFLY", "ZEBRA LONG WING", "BANDED ORANGE HELICONIAN", "BLUE MORPHO", "SOUTHERN DOGFACE", "INDRA SWALLOW", "WOOD SATYR", "CABBAGE WHITE", "RED CRACKER", "ULYSES", "SLEEPY ORANGE",
                    "ORANGE OAKLEAF", "ADONIS", "STRAITED QUEEN", "CAIRNS BIRDWING", "GREY HAIRSTREAK", "GREAT JAY", "SILVER SPOT SKIPPER", "EASTERN DAPPLE WHITE", "PURPLE HAIRSTREAK", "ATALA", "METALMARK",
                    "BLUE SPOTTED CROW", "GREAT EGGFLY", "PEACOCK", "AFRICAN GIANT SWALLOWTAIL", "RED ADMIRAL", "MONARCH", "ELBOWED PIERROT", "CHESTNUT", "MESTRA", "BLACK HAIRSTREAK", "CLEOPATRA", "POPINJAY",
                    "COPPER TAIL", "SOOTYWING", "BANDED PEACOCK", "EASTERN COMA", "CHECQUERED SKIPPER", "BECKERS WHITE", "JULIA", "CLOUDED SULPHUR", "LARGE MARBLE"};

            String[] sciName = {"Libytheana carinenta", "Parides childrenae", "Phocides pigmalion", "Papilio polytes", "Parnassius clodius", "Limenitis arthemis", "Anthocharis cardamines", "Chlosyne janais",
                    "Hasora schoenherr", " Limenitis archippus", "Diaethria anna", "Adelpha iphiclus", "Vanessa cardui", "Anaea aidea", "Danaus plexippus", "Iphicides podalirius", "Call0phrys niphon", "Idea leuconoe",
                    "Aglais milberti", "Lycaena helloides", " Polygonia interrogationis", "Idea leuconoe", "Astraptes fulgerator", "Heliconius melpomene", "Euphaedra neophron", "Cercyonis pegala", "Parnassius apollo",
                    "Siproeta epaphus", "Siproeta stelenes", "Papilio glaucus", "Battus philenor", "Phyciodes tharos", "Hypolimnas misippus", "Heliconius charithonia", "Dryadula phaetusa", "Morpho peleides",
                    "Zerene eurydice", "Papilio indra", "Megisto cymela", "Pieris rapae", "Hamadryas amphinome", "Papilio ulysses", "Eurema nicippe", "Kallima inachus", "Lysandra bellargus", "Danaus gilippus",
                    "Ornithoptera euphorion", "Strymon melinus", "Graphium eurypylus", "Epargyreus clarus ", "Eachloe ausonia", "Neozephyrus quercus", "Eumaeus atala", "Rhetus periander", "Euploea midamus",
                    "Hypolimnas bolina", "Aglais io", "Papilio antimachus", "Vanessa atalanta", "Danaus plexippus", "Caleta elna", "Parantica sita", "Mestra hypermestra", "Satyrium pruni", "Gonepteryx cleopatra",
                    "Stibochiona nicea", "Lycaena arota", "Hesperopsis alpheus", "Anartia fatima", "Polygonia comma", "Carterocephalus palaemon", "Pontia beckerii", "Dryas iulia", "Colias philodice", " Euchloe ausonides"};

            if (maxConfidence > 0.4){
                classified.setText("Classified as:");
                result.setText(classes[maxPos]);
                confScore.setText(String.valueOf(maxConfidence));
                scientific.setText("Scientific Name:");
                scientificName.setText(sciName[maxPos]);
            } else{
                classified.setText("Classified as:");
                result.setText("---");
                confScore.setText("---");
                scientific.setText("Scientific Name:");
                scientificName.setText("---");
            }


//            ListView listView = (ListView) findViewById(R.id.result1);
//            ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, scores1);
//            listView.setAdapter(adapter);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            } else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e){
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}