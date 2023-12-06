package com.example.onestopshop;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.common.util.concurrent.ListenableFuture;
import android.Manifest;

import java.io.File;

/**
 * CameraX screen
 */
public class CameraXFragment extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private PreviewView previewView;
    private Button takePhotoButton;
    private ImageCapture imageCapture;
    private OnImageCapturedListener onImageCapturedListener;
    private boolean isCameraReady;

    public CameraXFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_x, container, false);

        // Initialize UI components
        previewView = view.findViewById(R.id.viewFinder);
        takePhotoButton = view.findViewById(R.id.image_capture_button);
        isCameraReady = false;

        // Set up image capture use case
        imageCapture = new ImageCapture.Builder().build();


        // Set up camera and bind use cases
        requestCameraPermission();

        // Set up the listener for capturing photos
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCameraReady){
                    takePhoto();
                }
            }
        });

        return view;
    }



    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Set up use cases
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Unbind any use cases before binding
                cameraProvider.unbindAll();

                // Bind use cases to camera
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();


                cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture);
                // Camera is now ready
                isCameraReady = true;

            } catch (Exception e) {
                Log.e("CameraXFragment", "Error starting camera", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    // Implement capturing a photo and handling the result
    private void takePhoto() {
        if (imageCapture == null) {
            Log.e("CameraXFragment", "ImageCapture is null");
            return;
        }

        // Get the output directory for storing the captured photos
        File outputDirectory = requireContext().getFilesDir();

        // Create a unique file name for the photo
        String photoFileName = "photo_" + System.currentTimeMillis() + ".jpg";

        // Create the File object for the output photo
        File photoFile = new File(outputDirectory, photoFileName);

        // Create output options object
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        // Set up image capture listener
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(requireContext()), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                // You can set the result URI for the launching activity here
                Uri savedPhotoUri = Uri.fromFile(photoFile);

                // Notify the listener with the captured image URI
                if (onImageCapturedListener != null) {
                    onImageCapturedListener.onImageCaptured(savedPhotoUri);
                }
                // Finish the fragment to return to the hosting activity
                requireActivity().onBackPressed();

            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e("CameraXFragment", "Error taking photo", exception);
            }
        });


    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted, proceed with your camera-related tasks
            // For example, you can start the camera preview here
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, proceed with your camera-related tasks
                startCamera();
            } else {
                // Camera permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }
    public void setOnImageCapturedListener(OnImageCapturedListener listener) {
        this.onImageCapturedListener = listener;
    }
    public interface OnImageCapturedListener {
        void onImageCaptured(Uri imageUri);
    }


}