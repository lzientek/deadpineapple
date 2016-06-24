package com.deadpineapple.videoHelper.information;

import com.deadpineapple.videoHelper.TimeSpan;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by 15256 on 31/03/2016.
 */
public class VideoInformation {
    private String filePath;
    private String ffmpegResult;
    private TimeSpan duration;
    private String encoding;
    private String resolution;

    public String getFilePath() {
        return filePath;
    }

    public TimeSpan getDuration() {
        return duration;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getResolution() {
        return resolution;
    }

    public VideoInformation(String filePath) {
        ffmpegResult = "";
        this.filePath = filePath;
        extractData();
    }

    private void extractData() {
        Runtime rt = Runtime.getRuntime();
        Process proc = null;
        try {
            proc = rt.exec(new String[]{"ffmpeg", "-i", filePath});

            InputStream stdin = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);

            String line = null;

            while ((line = br.readLine()) != null) {
                ffmpegResult += line + "\n\r";
                if (line.contains("Duration:")) {
                    int durIndex = line.indexOf("Duration: ") + "Duration: ".length();
                    duration = new TimeSpan(line.substring(durIndex, durIndex + 11).trim());
                }
                if (line.contains("Stream #0:0(eng): Video:")) {
                    String[] videoInformations = line.split(", ");
                    encoding = videoInformations[0].trim().substring("Stream #0:0(eng): Video: ".length()).split(" ")[0];
                    for (String info : videoInformations) {
                        if (info.matches("([0-9])+x([0-9])+.+")) {
                            resolution = info.trim().split(" ")[0];
                        }
                    }
                }
            }

            int exitVal = proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * récupère l'image d'une video
     * @param imagePath chemin ou enregistrer votre image
     * @return sucess
     */
    public boolean generateAThumbnailImage(String imagePath) {
        Runtime rt = Runtime.getRuntime();
        Process proc = null;
        try {
            double startTumbTime = 3.0;
            if (duration.getMinutes() > 3){
                startTumbTime = 55.0;
            }

            proc = rt.exec(new String[]{"ffmpeg", "-y", "-nostats", "-loglevel", "0", "-ss",
                    Double.toString(startTumbTime), "-i", filePath, "-vframes", "1", "-f", "image2", imagePath});
            return proc.waitFor() == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


}
