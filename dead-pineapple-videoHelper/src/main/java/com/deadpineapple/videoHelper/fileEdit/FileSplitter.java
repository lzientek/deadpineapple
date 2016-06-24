package com.deadpineapple.videoHelper.fileEdit;

import com.deadpineapple.videoHelper.TimeSpan;
import com.deadpineapple.videoHelper.information.VideoInformation;


import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by 15256 on 31/03/2016.
 */
public class FileSplitter {
    private String filePath;
    private final static int MaximumVideoSizeInMinutes = 3;

    public FileSplitter(String filePath) {
        this.filePath = filePath;
    }

    public List<File> splitFile() throws Exception {
        List<File> files = new ArrayList<File>();
        File file = new File(filePath);//File read from Source folder to Split.

        int splitFileIndex = 0;
        String resultPath;
        TimeSpan startTime = new TimeSpan();
        TimeSpan duree = new TimeSpan(0, 3, 0);
        VideoInformation videoInformation = new VideoInformation(filePath);

        do {
            String extention = file.getName().substring(file.getName().lastIndexOf('.'));
            resultPath = new File(filePath).getParent() + File.separator
                    + splitFileIndex + "_" + file.getName().replace(extention, "")
                    .replace('.',' ')
                    .replace('-', ' ').replace('(', ' ').replace(')', ' ')
                    + extention;
            File resultFile = new File(resultPath);
            /*String ffmpeg = "ffmpeg -v quiet -y -i \"" + filePath + "\" -vcodec copy -acodec copy -ss "
                    + startTime + " -t " + duree + " -sn \"" + resultFile + "\"";
            */

            String[] ffmpeg = new String[]{"ffmpeg", "-v","quiet",  "-y", "-i",
                    filePath, "-vcodec", "copy", "-acodec", "copy", "-ss",
                    startTime.toString(), "-t", duree.toString(), "-sn",  resultFile.toString()};

            Process proc = Runtime.getRuntime().exec(ffmpeg);
            proc.waitFor();
            splitFileIndex++;
            files.add(resultFile);
            startTime.addMinutes(MaximumVideoSizeInMinutes);
        } while (videoInformation.getDuration().isGreaterThan(startTime));


        return files;
    }
}
