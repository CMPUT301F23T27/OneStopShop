package com.example.onestopshop;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.Toast;

import java.io.File;
//Citations: Use of CameraX documentation and ChatGPT (Dec 3) to understand binding to camera
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

        previewView = view.findViewById(R.id.viewFinder);
        takePhotoButton = view.findViewById(R.id.image_capture_button);
        isCameraReady = false;

        // Set up image capture use case
        imageCapture = new ImageCapture.Builder().build();



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

                Uri savedPhotoUri = Uri.fromFile(photoFile);

                // Notify the listener with the captured image URI
                if (onImageCapturedListener != null) {
                    onImageCapturedListener.onImageCaptured(savedPhotoUri);
                }

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
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);

        } else {
            // Permission is granted
            startCamera();
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted, start the camera
                    startCamera();
                } else {
                    // Permission is denied, handle it
                    Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();

                }
            });
    public void setOnImageCapturedListener(OnImageCapturedListener listener) {
        this.onImageCapturedListener = listener;
    }
    public interface OnImageCapturedListener {
        void onImageCaptured(Uri imageUri);
    }


}