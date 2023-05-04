package com.smsjuegos.quiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.smsjuegos.quiz.GameAztecStartVideoAct;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.databinding.ActivityDeclimarBinding;
import com.smsjuegos.quiz.model.SuccessResGetEventDetail;

public class DeclimarActivity extends AppCompatActivity {
ActivityDeclimarBinding binding ;
    private String eventId, eventCode, disclaimer;
    private SuccessResGetEventDetail.Result eventDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declimar);
         binding = DataBindingUtil.setContentView(this,R.layout.activity_declimar);
         if (getIntent().getExtras()!=null) {
             Intent intent = this.getIntent();
             Bundle bundle = intent.getExtras();
             eventDetails = (SuccessResGetEventDetail.Result) bundle.getSerializable("instructionID");
             eventId = eventDetails.getId();
             eventCode = getIntent().getExtras().getString("eventCode");
             disclaimer = eventDetails.getDisclaimer();

             Log.e("TAG", "eventDetailseventDetailseventDetails: " + eventId);
             Log.e("TAG", "eventDetailseventDetailseventDetails: " + eventCode);
             final String encoding = "UTF-8";
             final String mimeType = "text/html";
             // binding.tvInstruction.setText(data.getResult().get(0).getInstructions());
             binding.tvInstruction.loadDataWithBaseURL("", disclaimer,
                     mimeType, encoding, "");
         }
       //  binding.tvInstruction.setText(getString(R.string.desclemer) );
         binding.btnDownload.setOnClickListener(v -> {
            /* Bundle bundle = new Bundle();
             bundle.putSerializable("instructionID", );
           */
             startActivity(new Intent(getApplicationContext(),
                     GameAztecStartVideoAct.class)
                     // .putExtras(bundle )
                     .putExtra("videoUrl", eventDetails.getVideo())
                     .putExtra("eventId", eventId)
                     .putExtra("eventId", eventId)
                     .putExtra("eventCode", eventCode));
            /* startActivity(new Intent(getApplicationContext(),
                     DownloadAct.class)
                     .putExtra("eventId", eventId)
                     .putExtra("eventCode", eventCode));*/
         });
        /*Descargo de Responsabilidad
 
Yo, como usuario de la aplicación Secret Mission Society, por medio de la presente declaro que:
Tengo la mayoría de edad y que soy legalmente capaz para suscribir y asumir el presente descargo de responsabilidad, así como el de los menores de edad bajo mi cargo.
Entiendo y acepto que, la práctica de turismo y recreación en las áreas públicas de la ciudad de México, implican un riesgo que no puede ser completamente eliminado aun cuando exista prevención, cuidados, precaución instrucción o experiencia, lo que podrá conllevar a sufrir lesiones físicas leves, moderadas y severas, o inclusive la muerte, por lo tanto, es una decisión libre y voluntaria la práctica de la misma.
Conozco el contenido del presente formulario, así como todas las normas y disposiciones establecidas en el reglamento interno del área en dónde se llevará a cabo la actividad turística / recreativa; en consecuencia, acepto respetar y cumplir las mismas en mi visita al área de tránsito en donde se llevará a cabo el juego.
Entiendo que debo estar al pendiente del entorno ya que no es un espacio controlado.  El juego / actividad se lleva a cabo en vías públicas donde existe tránsito de automóviles y de personas que son ajenas a la activada que se lleva a cabo.
Soy consciente del riesgo que conlleva la práctica de estas actividades dentro de la zona metropolitana y los riesgos inherentes a esta área, por ello, me comprometo a mantener un comportamiento responsable que no aumente los riesgos para mi integridad y de las personas a mi cargo.
En caso de incumplimiento de los parámetros normativos, así como también con el Reglamento interno del área para los visitantes, que causen un incidente en contra de mi integridad, del Patrimonio Nacional del Estado y de los bienes e infraestructura del área, asumiré los costos y gastos administrativos, operativos y logísticos generados por dicha acción.
Por último, permito el libre uso de mi nombre y cualquier dato de mi persona obtenida durante mi recorrido dentro del área de tránsito de turismo y actividad recreativa, para fines estadísticos, y turísticos.   Entiendo que, los términos del presente formulario son de obligatorio cumplimiento y que he firmado este documento por mi propia voluntad. Declaro que toda la información detallada es veraz y de comprobación por parte de la Autoridad competente, por lo cual me comprometo a asumir la responsabilidad administrativa, civil y/o penal que de la misma se derive, incluso por daños a terceros.
Al dar clic en el siguiente botón, antes de comenzar la activad, acepto lo antes aquí descrito. */
    }
}