package de.yanneckreiss.cameraxtutorial.ui.camera

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

// Function to perform contour detection on a Sudoku puzzle image
fun detectSudokuContours(bitmap: Bitmap): List<MatOfPoint?> {
    val sudokuImage = Mat()
    Utils.bitmapToMat(bitmap, sudokuImage)

    // Convert the image to grayscale for better contour detection
    val grayImage = Mat()
    Imgproc.cvtColor(sudokuImage, grayImage, Imgproc.COLOR_RGBA2GRAY)

    // Apply Gaussian blur to reduce noise
    val blurredImage = Mat()
    Imgproc.GaussianBlur(grayImage, blurredImage, Size(9.0, 9.0), 2.0, 2.0)

    // Perform edge detection using Canny
    val edges = Mat()
    Imgproc.Canny(blurredImage, edges, 50.0, 150.0)

    // Find contours in the edge-detected image
    val contours = ArrayList<MatOfPoint>()
    val hierarchy = Mat()
    Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)

    // Filter the contours to find the largest one (the outer boundary of Sudoku)
    val largestContour = contours.maxByOrNull { Imgproc.contourArea(it) }

    // Release Mats to free up memory
    grayImage.release()
    blurredImage.release()
    edges.release()
    hierarchy.release()

    return listOf(largestContour)
}
