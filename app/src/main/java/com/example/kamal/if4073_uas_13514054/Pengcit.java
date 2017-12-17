package com.example.kamal.if4073_uas_13514054;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamal on 2017-12-12.
 */

public class Pengcit {

    public static final int PICK_IMAGE = 1;
    private static final String TAG = "OCV::Pengcit";

    static {
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV successfully loaded");
        } else {
            Log.d(TAG, "OpenCV not loaded");
        }
    }

    public Pengcit() {

    }

    public static Bitmap getGrayscaleBitmap(Bitmap bmp_rgba) {
        Mat mat_bgr = new Mat();
        Mat mat_grayscale = new Mat();

        Utils.bitmapToMat(bmp_rgba, mat_bgr);
        Imgproc.cvtColor(mat_bgr, mat_bgr, Imgproc.COLOR_RGBA2BGR);
        Imgproc.cvtColor(mat_bgr, mat_grayscale, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(mat_grayscale, mat_grayscale, Imgproc.COLOR_GRAY2RGBA, 4);

        Bitmap bmp_grayscale = Bitmap.createBitmap(mat_grayscale.cols(), mat_grayscale.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_grayscale, bmp_grayscale);

        return bmp_grayscale;
    }

    public static boolean isMatGrayscale(Mat mat) {
//        Size size = mat.size();
//        for (int i = 0; i < size.height; ++i) {
//            for (int j = 0; j < size.width; ++j) {
//                double[] data = mat.get(i, j);
//                if ((data[0] != data[1]) || (data[1] != data[2]) || (data[0] != data[2])) {
//                    return false;
//                }
//            }
//        }
//
//        return true;

        Size size = mat.size();
        double[] data = mat.get(0, 0);
        return ((data[0] == data[1]) && (data[0] == data[2]) && (data[0] == data[3])
             && (data[1] == data[2]) && (data[1] == data[3])
             && (data[2] == data[3]));
    }

    public static boolean isBitmapGrayscale(Bitmap bmp) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);

        return isMatGrayscale(mat);
    }

    public static Bitmap getHistogramBitmap(Bitmap bmp, int width, int height) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat);

        // split the frames in multiple images
        ArrayList<Mat> images = new ArrayList<>();
        Core.split(mat, images);

        // set the number of bins at 256
        MatOfInt hist_size = new MatOfInt(256);
        // only one channel
        MatOfInt channels = new MatOfInt(0);
        // set the ranges
        MatOfFloat hist_range = new MatOfFloat(0, 256);

        // compute the histograms for the B, G, R, and Gray components
        Mat hist_gray = new Mat();
        Mat hist_blue = new Mat();
        Mat hist_green = new Mat();
        Mat hist_red = new Mat();

        if (isBitmapGrayscale(bmp)) {
            Imgproc.calcHist(images.subList(0, 1), channels, new Mat(), hist_gray, hist_size, hist_range, false);
        } else {
            Imgproc.calcHist(images.subList(0, 1), channels, new Mat(), hist_blue, hist_size, hist_range, false);
            Imgproc.calcHist(images.subList(1, 2), channels, new Mat(), hist_green, hist_size, hist_range, false);
            Imgproc.calcHist(images.subList(2, 3), channels, new Mat(), hist_red, hist_size, hist_range, false);
        }

        // draw the histogram
        int hist_w = width; // width of the histogram image
        int hist_h = height; // height of the histogram image
        int bin_w = (int) Math.round(hist_w / hist_size.get(0, 0)[0]);

        Mat mat_hist = new Mat(hist_h, hist_w, CvType.CV_8UC3, new Scalar(0, 0, 0));

        // normalize the result to [0, histImage.rows()]
        if (isBitmapGrayscale(bmp)) {
            Core.normalize(hist_gray, hist_gray, 0, mat_hist.rows(), Core.NORM_MINMAX, -1, new Mat());
        } else {
            Core.normalize(hist_blue, hist_blue, 0, mat_hist.rows(), Core.NORM_MINMAX, -1, new Mat());
            Core.normalize(hist_green, hist_green, 0, mat_hist.rows(), Core.NORM_MINMAX, -1, new Mat());
            Core.normalize(hist_red, hist_red, 0, mat_hist.rows(), Core.NORM_MINMAX, -1, new Mat());
        }

        // effectively draw the histogram(s)
        if (isBitmapGrayscale(bmp)) {
            for (int i = 1; i < hist_size.get(0, 0)[0]; ++i) {
                Imgproc.line(mat_hist, new Point(bin_w * (i - 1), hist_h - Math.round(hist_gray.get(i - 1, 0)[0])),
                        new Point(bin_w * (i), hist_h - Math.round(hist_gray.get(i, 0)[0])), new Scalar(255, 255, 255), 2, 8, 0);
            }
        } else {
            for (int i = 1; i < hist_size.get(0, 0)[0]; ++i) {
                Imgproc.line(mat_hist, new Point(bin_w * (i - 1), hist_h - Math.round(hist_blue.get(i - 1, 0)[0])),
                        new Point(bin_w * (i), hist_h - Math.round(hist_blue.get(i, 0)[0])), new Scalar(255, 0, 0), 2, 8, 0);
                Imgproc.line(mat_hist, new Point(bin_w * (i - 1), hist_h - Math.round(hist_green.get(i - 1, 0)[0])),
                        new Point(bin_w * (i), hist_h - Math.round(hist_green.get(i, 0)[0])), new Scalar(0, 255, 0), 2, 8, 0);
                Imgproc.line(mat_hist, new Point(bin_w * (i - 1), hist_h - Math.round(hist_red.get(i - 1, 0)[0])),
                        new Point(bin_w * (i), hist_h - Math.round(hist_red.get(i, 0)[0])), new Scalar(0, 0, 255), 2, 8, 0);
            }
        }

        Imgproc.cvtColor(mat_hist, mat_hist, Imgproc.COLOR_BGR2RGBA, 4);
        Bitmap bmp_hist = Bitmap.createBitmap(mat_hist.cols(), mat_hist.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_hist, bmp_hist);

        return bmp_hist;
    }

    public static Bitmap getEqualizeBitmap(Bitmap bmp) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);

        Mat mat_eq = new Mat();
        Imgproc.equalizeHist(mat, mat_eq);

        Imgproc.cvtColor(mat_eq, mat_eq, Imgproc.COLOR_GRAY2RGBA, 4);

        Bitmap bmp_eq = Bitmap.createBitmap(mat_eq.cols(), mat_eq.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_eq, bmp_eq);

        return bmp_eq;
    }

    public static Bitmap getOtsuThresholdedBitmap(Bitmap bmp, int thresh, int maxval) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2BGR);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);

        Mat mat_otsu = new Mat();
        Imgproc.threshold(mat, mat_otsu, thresh, maxval, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);

        Bitmap bmp_otsu = Bitmap.createBitmap(mat_otsu.cols(), mat_otsu.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_otsu, bmp_otsu, true);

        return bmp_otsu;
    }

    public static Bitmap getBlurredBitmap(Bitmap bmp, int width, int height) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);

        Mat mat_blur = new Mat();
        Imgproc.blur(mat, mat_blur, new Size(width, height));

        Bitmap bmp_blur = Bitmap.createBitmap(mat_blur.cols(), mat_blur.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_blur, bmp_blur, true);

        return bmp_blur;
    }

    public static Bitmap getGaussianBlurredBitmap(Bitmap bmp, int width, int height, double sigmaX, double sigmaY) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);

        Mat mat_blur = new Mat();
        Imgproc.GaussianBlur(mat, mat_blur, new Size(width, height), sigmaX, sigmaY);

        Bitmap bmp_blur = Bitmap.createBitmap(mat_blur.cols(), mat_blur.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_blur, bmp_blur, true);

        return bmp_blur;
    }

    public static Bitmap getMedianBlurredBitmap(Bitmap bmp, int ksize) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);

        Mat mat_blur = new Mat();
        Imgproc.medianBlur(mat, mat_blur, ksize);

        Bitmap bmp_blur = Bitmap.createBitmap(mat_blur.cols(), mat_blur.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_blur, bmp_blur, true);

        return bmp_blur;
    }

    public static Bitmap getHorizontalEdgeFilteredBitmap(Bitmap bmp, int ksize) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2BGR);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);

        Mat mat_edge = new Mat();
        Imgproc.Sobel(mat, mat_edge, CvType.CV_16S, 0, 1, ksize, 1, 0);
        Core.convertScaleAbs(mat_edge, mat_edge);

        Bitmap bmp_edge = Bitmap.createBitmap(mat_edge.cols(), mat_edge.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_edge, bmp_edge, true);

        return bmp_edge;
    }

    public static Bitmap getVerticalEdgeFilteredBitmap(Bitmap bmp, int ksize) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2BGR);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);

        Mat mat_edge = new Mat();
        Imgproc.Sobel(mat, mat_edge, CvType.CV_16S, 1, 0, ksize, 1, 0);
        Core.convertScaleAbs(mat_edge, mat_edge);

        Bitmap bmp_edge = Bitmap.createBitmap(mat_edge.cols(), mat_edge.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_edge, bmp_edge, true);

        return bmp_edge;
    }

    public static Bitmap getEdgeFilteredBitmap(Bitmap bmp, int ksize) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2BGR);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);

        Mat mat_edge = new Mat();
//        Mat kernel = new Mat(3, 3, CvType.CV_32F) {
//            {
//                put(0, 0, 1);
//                put(0, 1, 0);
//                put(0, 2, -1);
//
//                put(1, 0, 2);
//                put(1, 1, 0);
//                put(1, 2, -2);
//
//                put(2, 0, 1);
//                put(2, 1, 0);
//                put(2, 2, -1);
//            }
//        };

//        Imgproc.filter2D(mat, mat_edge, -1, kernel);
        Imgproc.Sobel(mat, mat_edge, CvType.CV_16S, 1, 1, ksize, 1, 0);
        Core.convertScaleAbs(mat_edge, mat_edge);

        Bitmap bmp_edge = Bitmap.createBitmap(mat_edge.cols(), mat_edge.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_edge, bmp_edge, true);

        return bmp_edge;
    }

    public static Mat getOptimizedImageDim(Mat mat) {
        // init
        Mat mat_padded = new Mat();

        // get the optimal rows size for dft
        int add_pixel_rows = Core.getOptimalDFTSize(mat.rows());

        // get the optimal cols size for dft
        int add_pixel_cols = Core.getOptimalDFTSize(mat.cols());

        // apply the optimal cols and rows size to the image
        Core.copyMakeBorder(mat, mat_padded, 0, add_pixel_rows - mat.rows(), 0, add_pixel_cols - mat.cols(), Core.BORDER_CONSTANT, Scalar.all(0));

        return mat_padded;
    }

    public static Mat getDFTMat(Mat mat) {
        // optimize the dimension of the loaded image
        Mat mat_padded = getOptimizedImageDim(mat);
        mat_padded.convertTo(mat_padded, CvType.CV_32F);

        // prepare the image planes to obtain the complex image
        List<Mat> planes = new ArrayList<>();
        planes.add(mat_padded);
        planes.add(Mat.zeros(mat_padded.size(), CvType.CV_32F));

        // prepare a complex image for performing the dft
        Mat mat_complex = new Mat();
        Core.merge(planes, mat_complex);

        // dft
        Core.dft(mat_complex, mat_complex);

        return mat_complex;
    }

    public static Mat getInverseDFTMat(Mat mat) {
        Core.idft(mat, mat);

        List<Mat> planes = new ArrayList<>();
        Mat mat_restored = new Mat();
        Core.split(mat, planes);
        Core.normalize(planes.get(0), mat_restored, 0, 255, Core.NORM_MINMAX);

        // move back the Mat to 8 bit, in order to proper show the result
        mat_restored.convertTo(mat_restored, CvType.CV_8U);

        return mat_restored;
    }

    public static void shiftDFT(Mat image) {
        image = image.submat(new Rect(0, 0, image.cols() & -2, image.rows() & -2));
        int cx = image.cols() / 2;
        int cy = image.rows() / 2;

        Mat q0 = new Mat(image, new Rect(0, 0, cx, cy));
        Mat q1 = new Mat(image, new Rect(cx, 0, cx, cy));
        Mat q2 = new Mat(image, new Rect(0, cy, cx, cy));
        Mat q3 = new Mat(image, new Rect(cx, cy, cx, cy));

        Mat tmp = new Mat();
        q0.copyTo(tmp);
        q3.copyTo(q0);
        tmp.copyTo(q3);

        q1.copyTo(tmp);
        q2.copyTo(q1);
        tmp.copyTo(q2);
    }

    public static List<Mat> getShiftDFTMat(Mat mat) {
        // init
        List<Mat> planes = new ArrayList<>();
        Mat magnitude = new Mat();
        Mat angle = new Mat();

        // split the complex image in two planes
        Core.split(mat, planes);

        // compute the magnitude and the angle
        Core.cartToPolar(planes.get(0), planes.get(1), magnitude, angle);

        // optionally reorder the 4 quadrants of the magnitude image
        shiftDFT(magnitude);

        List<Mat> results = new ArrayList<>();
        results.add(magnitude);
        results.add(angle);

        return results;
    }

    public static Mat getInverseShiftDFTMat(List<Mat> shift) {
        Mat magnitude = shift.get(0);
        Mat angle = shift.get(1);
        shiftDFT(magnitude);

        Mat plane_1 = new Mat();
        Mat plane_2 = new Mat();

        Core.polarToCart(magnitude, angle, plane_1, plane_2);
        List<Mat> planes = new ArrayList<>();
        planes.add(plane_1);
        planes.add(plane_2);

        Mat inverse_shift = new Mat();
        Core.merge(planes, inverse_shift);

        return inverse_shift;
    }

    public static Bitmap getMagnitudeSpectrumBitmap(Mat magnitude) {
        Mat mat_mag = new Mat();

        // move to a logarithmic scale
        Core.add(Mat.ones(magnitude.size(), CvType.CV_32F), magnitude, mat_mag);
        Core.log(mat_mag, mat_mag);

        // normalize the magnitude image for the visualization since both JavaFX
        // and OpenCV need images with value between 0 and 255
        // convert back to CV_8UC1
        mat_mag.convertTo(mat_mag, CvType.CV_8UC1);
        Core.normalize(mat_mag, mat_mag, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);

        Bitmap bmp_mag = Bitmap.createBitmap(mat_mag.cols(), mat_mag.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_mag, bmp_mag, true);

        return bmp_mag;
    }

    public static List<Bitmap> getDFT(Bitmap bmp) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2BGR);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);

        Mat mat_complex = getDFTMat(mat);

        List<Mat> shifts = getShiftDFTMat(mat_complex);
        Mat magnitude = shifts.get(0);
        Mat angle = shifts.get(1);
        Bitmap bmp_mag = getMagnitudeSpectrumBitmap(magnitude);

        int x = magnitude.width() / 2;
        int y = magnitude.height() / 2;
        int radius = 10;
        Imgproc.circle(magnitude, new Point(x, y), radius, new Scalar(0), -1);
        Imgproc.circle(angle, new Point(x, y), radius, new Scalar(0), -1);
        Bitmap bmp_mag_modified = getMagnitudeSpectrumBitmap(magnitude);

        List<Mat> shifts_modified = new ArrayList<>();
        shifts_modified.add(magnitude);
        shifts_modified.add(angle);
        Mat mat_inverse_shift = getInverseShiftDFTMat(shifts_modified);

        Mat mat_restored = getInverseDFTMat(mat_inverse_shift);

        Bitmap bmp_restored = Bitmap.createBitmap(mat_restored.cols(), mat_restored.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_restored, bmp_restored, true);

        List<Bitmap> result = new ArrayList<>();
        result.add(bmp_mag);
        result.add(bmp_mag_modified);
        result.add(bmp_restored);

        return result;
    }

    public static Bitmap getHSVBitmap(Bitmap bmp) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);

        Mat mat_bgr = new Mat();
        Imgproc.cvtColor(mat, mat_bgr, Imgproc.COLOR_RGBA2BGR);

        Mat mat_hsv = new Mat();
        Imgproc.cvtColor(mat_bgr, mat_hsv, Imgproc.COLOR_BGR2HSV);

        Bitmap bmp_hsv = Bitmap.createBitmap(mat_hsv.cols(), mat_hsv.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_hsv, bmp_hsv, true);

        return bmp_hsv;
    }

    public static Bitmap getSkinFilteredBitmap(Bitmap bmp) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);

        Mat mat_bgr = new Mat();
        Imgproc.cvtColor(mat, mat_bgr, Imgproc.COLOR_RGBA2BGR);

        Mat mat_hsv = new Mat();
        Imgproc.cvtColor(mat_bgr, mat_hsv, Imgproc.COLOR_BGR2HSV);

        Mat mat_skin = Mat.zeros(mat.size(), mat.type());
        double[] black = {
                0, 0, 0, 255
        };

        for (int i = 0; i < mat.height(); ++i) {
            for (int j = 0; j < mat.width(); ++j) {
                double[] pixel_rgba = mat.get(i, j);
                double[] pixel_hsv = mat_hsv.get(i, j);

                double red = pixel_rgba[0];
                double green = pixel_rgba[1];
                double blue = pixel_rgba[2];
                double alpha = pixel_rgba[3];

                double hue = pixel_hsv[0];
                double saturation = pixel_hsv[1];
                double value = pixel_hsv[2];

                if ((hue >= 0) && (hue <= 35.416666667)
                && (saturation >= 58.65) && (saturation <= 173.4)
                && (red >= 95) && (green >= 40) && (blue >= 20)
                && (red > green) && (red > blue) && (Math.abs(red - green) > 15) && (alpha > 15)) {
                    mat_skin.put(i, j, pixel_rgba);
                } else {
                    mat_skin.put(i, j, black);
                }
            }
        }

        Bitmap bmp_skin = Bitmap.createBitmap(mat_skin.cols(), mat_skin.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_skin, bmp_skin, true);

        return bmp_skin;
    }

    public static int getA(Mat mat, int row, int col) {
        int count = 0;
        double default_value = 2;

        double[] p1 = {default_value};
        double[] p2 = {default_value};
        double[] p3 = {default_value};
        double[] p4 = {default_value};
        double[] p5 = {default_value};
        double[] p6 = {default_value};
        double[] p7 = {default_value};
        double[] p8 = {default_value};
        double[] p9 = {default_value};

        p1 = mat.get(row, col);

        if (row - 1 > 0) {
            p2 = mat.get(row-1, col);
        }

        if (row - 1 > 0 && col + 1 < mat.cols()) {
            p3 = mat.get(row-1, col+1);
        }

        if (col + 1 < mat.cols()) {
            p4 = mat.get(row, col+1);
        }

        if (row + 1 < mat.rows() && col + 1 < mat.cols()) {
            p5 = mat.get(row+1, col+1);
        }

        if (row + 1 < mat.rows()) {
            p6 = mat.get(row+1, col);
        }

        if (row + 1 < mat.rows() && col - 1 > 0) {
            p7 = mat.get(row+1, col-1);
        }

        if (col - 1 > 0) {
            p8 = mat.get(row, col-1);
        }

        if (row - 1 > 0 && col - 1 > 0) {
            p9 = mat.get(row-1, col-1);
        }


        if (p2[0] == 0 && p3[0] == 1) {
            ++count;
        }

        if (p3[0] == 0 && p4[0] == 1) {
            ++count;
        }

        if (p4[0] == 0 && p5[0] == 1) {
            ++count;
        }

        if (p5[0] == 0 && p6[0] == 1) {
            ++count;
        }

        if (p6[0] == 0 && p7[0] == 1) {
            ++count;
        }

        if (p7[0] == 0 && p8[0] == 1) {
            ++count;
        }

        if (p8[0] == 0 && p9[0] == 1) {
            ++count;
        }

        if (p9[0] == 0 && p2[0] == 1) {
            ++count;
        }

        return count;
    }

    public static int getB(Mat mat, int row, int col) {
        double[] v1 = mat.get(row+1, col);
        double[] v2 = mat.get(row+1, col+1);
        double[] v3 = mat.get(row, col+1);
        double[] v4 = mat.get(row-1, col-1);
        double[] v5 = mat.get(row-1, col);
        double[] v6 = mat.get(row, col-1);
        double[] v7 = mat.get(row+1, col-1);
        double[] v8 = mat.get(row-1, col+1);

        return (int) Math.abs(v1[0] + v2[0] + v3[0] + v4[0] + v5[0] + v6[0] + v7[0] + v8[0]);
    }

    public static Bitmap getZhangSuenThinnedBitmap(Bitmap bmp) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bmp, mat, true);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2BGR);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);

        Mat mat_otsu = new Mat();
        Imgproc.threshold(mat, mat_otsu, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);

        for (int i = 0; i < mat_otsu.height(); ++i) {
            for (int j = 0; j < mat_otsu.width(); ++j) {
                double[] pixel = mat_otsu.get(i, j);
                for (int k = 0; k < pixel.length; ++k) {
                    pixel[k] /= 255;
                }
                mat_otsu.put(i, j, pixel);
            }
        }

        int a, b;
        List<int[]> pointsToChange = new ArrayList<>();
        boolean hasChange;
        do {
            Log.d(TAG, "dar");
            hasChange = false;
            for (int row = 1; row + 1 < mat_otsu.rows(); ++row) {
                for (int col = 1; col + 1 < mat_otsu.cols(); ++col) {
                    a = getA(mat_otsu, row, col);
                    b = getB(mat_otsu, row, col);

                    if (mat_otsu.get(row, col)[0] == 1 && 2 <= b && b <= 6 && a == 1
                            && (mat_otsu.get(row - 1, col)[0] * mat_otsu.get(row, col + 1)[0] * mat_otsu.get(row + 1, col)[0] == 0)
                            && (mat_otsu.get(row, col + 1)[0] * mat_otsu.get(row + 1, col)[0] * mat_otsu.get(row, col - 1)[0] == 0)) {

                        int[] point = {
                                row, col
                        };
                        pointsToChange.add(point);
                        hasChange = true;
                    }
                }
            }

            double[] black = {0};
            for (int[] point: pointsToChange) {
                mat_otsu.put(point[0], point[1], black);
            }
            pointsToChange.clear();

            Log.d(TAG, "dor");
            for (int row = 1; row + 1 < mat_otsu.rows(); ++row) {
                for (int col = 1; col + 1 < mat_otsu.cols(); ++col) {
                    a = getA(mat_otsu, row, col);
                    b = getB(mat_otsu, row, col);

                    if (mat_otsu.get(row, col)[0] == 1 && 2 <= b && b <= 6 && a == 1
                            && (mat_otsu.get(row - 1, col)[0] * mat_otsu.get(row, col + 1)[0] * mat_otsu.get(row, col - 1)[0] == 0)
                            && (mat_otsu.get(row - 1, col)[0] * mat_otsu.get(row + 1, col)[0] * mat_otsu.get(row, col - 1)[0] == 0)) {

                        int[] point = {
                                row, col
                        };
                        pointsToChange.add(point);
                        hasChange = true;
                    }
                }
            }

            for (int[] point: pointsToChange) {
                mat_otsu.put(point[0], point[1], black);
            }
            pointsToChange.clear();

        } while (hasChange);
        Log.d(TAG, "keluar");

        for (int i = 0; i < mat_otsu.height(); ++i) {
            for (int j = 0; j < mat_otsu.width(); ++j) {
                double[] pixel = mat_otsu.get(i, j);
                for (int k = 0; k < pixel.length; ++k) {
                    pixel[k] *= 255;
                }
                mat_otsu.put(i, j, pixel);
            }
        }

        Bitmap bmp_thin = Bitmap.createBitmap(mat_otsu.cols(), mat_otsu.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat_otsu, bmp_thin, true);

        return bmp_thin;
    }
}
