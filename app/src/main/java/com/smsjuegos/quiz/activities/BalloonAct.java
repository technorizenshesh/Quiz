package com.smsjuegos.quiz.activities;

import android.animation.Animator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.smsjuegos.quiz.R;
import com.smsjuegos.quiz.utility.SoundUtils;


import android.animation.ObjectAnimator;
import android.util.Log;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.math.Vector3;
import com.smsjuegos.quiz.utility.Vector3Evaluator;

public class BalloonAct extends AppCompatActivity {
    private ArFragment arFragment;
    private Handler handler = new Handler(); // Handler to post delayed tasks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balloon);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            addBalloon(anchor);
        });
    }



    private void addBalloon(Anchor anchor) {
        //AnchorNode anchorNode = new AnchorNode(anchor);
       // anchorNode.setParent(arFragment.getArSceneView().getScene());

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ModelRenderable.builder()
                    .setSource(this, Uri.parse("http://appsmsjuegos.com/Quiz/uploads/Baloon.png"))
                    .build()
                    .thenAccept(modelRenderable -> {
                        TransformableNode modelNode = new TransformableNode(arFragment.getTransformationSystem());
                        modelNode.setRenderable(modelRenderable);
                        modelNode.setParent(anchorNode);
                    });
        }*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ViewRenderable.builder()
                    .setView(this, R.layout.image_view_render) // Inflate the view for the balloon
                    .build()
                    .thenAccept(viewRenderable -> {
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        anchorNode.setParent(arFragment.getArSceneView().getScene());

                        TransformableNode imageNode = new TransformableNode(arFragment.getTransformationSystem());
                        imageNode.setRenderable(viewRenderable);
                        imageNode.setParent(anchorNode);

                        // Schedule the balloon burst after 5 seconds
                       /* handler.postDelayed(() -> {
                            onBalloonBurst(anchorNode);
                        }, 5000); // 5000 milliseconds = 5 seconds*/

                        // Start animations after setting up the balloon
                        startBalloonAnimation(imageNode);

                    });



        }

    }




    private void startBalloonAnimation(TransformableNode balloonNode) {
        float endX = 0.5f; // Adjust as needed
        float endY = 0.5f; // Adjust as needed
        long duration = 2000; // Animation duration in milliseconds

        // Animate movement from right to left
        ObjectAnimator animatorX = ObjectAnimator.ofObject(balloonNode, "localPosition",
                new Vector3Evaluator(), new Vector3(0f, 0f, 0f), new Vector3(endX, 0f, 0f));
        animatorX.setDuration(duration);
        animatorX.start();

        // Animate movement from bottom to top
        ObjectAnimator animatorY = ObjectAnimator.ofObject(balloonNode, "localPosition",
                new Vector3Evaluator(), new Vector3(endX, 0f, 0f), new Vector3(endX, endY, 0f));
        animatorY.setDuration(duration);
        animatorY.setStartDelay(duration);
        animatorY.start();

        // Trigger balloon burst after animations
        animatorY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                onBalloonBurst(balloonNode);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

   /* private void onBalloonBurst(AnchorNode balloonNode) {
        balloonNode.setRenderable(null); // Hide the balloon
        SoundUtils.playBlastSound(this);
    }*/


    private void onBalloonBurst(Node balloonNode) {
        if (balloonNode != null) {
            balloonNode.setRenderable(null); // Hide the balloon
            SoundUtils.playBlastSound(this);// Play the blast sound
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            arFragment.getArSceneView().resume();
        } catch (CameraNotAvailableException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        arFragment.getArSceneView().pause();
    }

}