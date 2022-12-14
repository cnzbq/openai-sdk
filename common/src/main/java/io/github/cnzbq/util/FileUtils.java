package io.github.cnzbq.util;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

/**
 * copy org.springframework.util.FileCopyUtils
 * copy org.springframework.util.StreamUtils
 */
public class FileUtils {
    public static final int BUFFER_SIZE = 4096;

    /**
     * Copy the contents of the given input File into a new byte array.
     *
     * @param in the file to copy from
     * @return the new byte array that has been copied to
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(File in) throws IOException {
        return copyToByteArray(Files.newInputStream(in.toPath()));
    }

    /**
     * Copy the contents of the given InputStream into a new byte array.
     * Closes the stream when done.
     *
     * @param in the stream to copy from (may be {@code null} or empty)
     * @return the new byte array that has been copied to (possibly empty)
     * @throws IOException in case of I/O errors
     */
    public static byte[] copyToByteArray(InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy1(in, out);
        return out.toByteArray();
    }


    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * Closes both streams when done.
     *
     * @param in  the stream to copy from
     * @param out the stream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy1(InputStream in, OutputStream out) throws IOException {
        try {
            return copy(in, out);
        } finally {
            close(in);
            close(out);
        }
    }

    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * <p>Leaves both streams open when done.
     *
     * @param in  the InputStream to copy from
     * @param out the OutputStream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }


    /**
     * Attempt to close the supplied {@link Closeable}, silently swallowing any
     * exceptions.
     *
     * @param closeable the {@code Closeable} to close
     */
    private static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException ex) {
            // ignore
        }
    }


    /**
     * ??????URL???Base64??????
     *
     * @param imgUrl ??????URL
     * @return Base64??????
     */
    public static byte[] imageUrlToBase64(String imgUrl) throws IOException {
        if (StringUtils.isBlank(imgUrl)) {
            return new byte[0];
        }

        URL url;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;

        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();

            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();

            //????????????Buffer?????????
            byte[] buffer = new byte[1024];
            //??????????????????????????????????????????-1???????????????????????????
            int len = 0;
            //??????????????????buffer????????????????????????
            while ((len = is.read(buffer)) != -1) {
                //???????????????buffer???????????????????????????????????????????????????????????????len?????????????????????
                outStream.write(buffer, 0, len);
            }

            return outStream.toByteArray();
        } finally {
            if (is != null) {
                is.close();
            }
            if (outStream != null) {
                outStream.close();
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
    }

    /**
     * ????????????
     *
     * @param file ????????????
     * @throws IOException /
     */
    public static byte[] getImageCom(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return imageCom(inputStream);
        }
    }

    /**
     * ????????????
     *
     * @param imgByte ??????byte??????
     * @throws IOException /
     */
    public static byte[] getImageCom(byte[] imgByte) throws IOException {
        try (ByteArrayInputStream byteInput = new ByteArrayInputStream(imgByte);) {
            return imageCom(byteInput);
        }
    }

    private static byte[] imageCom(InputStream inputStream) throws IOException {
        BufferedImage bufImg = ImageIO.read(inputStream);
        // ????????????,??????????????????byte??????
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //??????????????????,?????????????????????
        BufferedImage bufferedImage = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics().drawImage(bufImg, 0, 0, Color.WHITE, null);
        //?????????jpg???????????????,???????????????OSS??????????????????????????????????????????
        ImageIO.write(bufferedImage, "jpg", bos);
        return bos.toByteArray();
    }
}
