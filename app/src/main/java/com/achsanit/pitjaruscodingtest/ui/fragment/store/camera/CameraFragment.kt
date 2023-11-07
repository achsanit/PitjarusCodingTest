package com.achsanit.pitjaruscodingtest.ui.fragment.store.camera

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.achsanit.pitjaruscodingtest.R
import com.achsanit.pitjaruscodingtest.databinding.FragmentCameraBinding
import com.achsanit.pitjaruscodingtest.ui.activity.MainActivity
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: Executor
    private lateinit var cameraProvider: ProcessCameraProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outputDirectory = getOutputDirectory()
        cameraExecutor = ContextCompat.getMainExecutor(requireContext())

        if ((activity as MainActivity).hasCameraPermission()) {
            startCamera()
        } else {
            (activity as MainActivity).requestCameraPermission()
        }

        with(binding) {
            ibBackToolbar.setOnClickListener {
                findNavController().popBackStack()
            }

            fabSaveImage.setOnClickListener {
                takePhoto()
            }
        }

    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Timber.e(exc,"Photo capture failed: ${exc.message}")
                    val reason = when (exc.imageCaptureError) {
                        ImageCapture.ERROR_UNKNOWN -> "unknown error"
                        ImageCapture.ERROR_FILE_IO -> "unable to save file"
                        ImageCapture.ERROR_CAPTURE_FAILED -> "capture failed"
                        ImageCapture.ERROR_CAMERA_CLOSED -> "camera closed"
                        ImageCapture.ERROR_INVALID_CAMERA -> "invalid camera"
                        else -> "unknown error"
                    }
                    val msg = "Photo capture failed: $reason"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    Timber.e( msg)
                    exc.printStackTrace()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri: Uri = Uri.fromFile(photoFile)

                    findNavController().previousBackStackEntry?.savedStateHandle?.set("imageUri", savedUri)
                    findNavController().popBackStack()
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener( {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            //For image capture purpose
            imageCapture = ImageCapture.Builder()
                .build()

            // Select FRONT camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Timber.tag(TAG).e(exc, "Use case binding failed")
            }

        }, cameraExecutor)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = context?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val TAG = "CameraFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}