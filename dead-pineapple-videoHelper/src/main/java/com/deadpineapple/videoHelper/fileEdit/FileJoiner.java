package com.deadpineapple.videoHelper.fileEdit;

import java.io.*;
import java.util.List;

/**
 * Created by 15256 on 31/03/2016.
 */
public class FileJoiner {
    private List<File> files;
    String fileName;

    public FileJoiner(List<File> files, String fileName) {
        this.files = files;
        this.fileName = fileName;
    }

    public File joinFiles() throws IOException, InterruptedException {
        File resultFile = new File(fileName);
        File videoListFile = new File(resultFile.getParent(),
                "lst" + resultFile.getName().substring(0, resultFile.getName().indexOf('.'))
                        + ".rftxt");

        videoListFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(videoListFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (File oldVideoFiles : files) {
            bw.write("file '" + oldVideoFiles.getAbsolutePath() + "'");
            bw.newLine();
        }

        bw.close();

        fos.close();
        /*String ffmpegCmd = "ffmpeg -v quiet -safe 0 -y -f concat -i \""
                + videoListFile.getAbsolutePath()
                + "\" -c copy \""
                + resultFile.getAbsolutePath() + "\"";
        */
        String[] ffmpegCmd = new String[]{"ffmpeg", "-v", "quiet", "-safe", "0", "-y", "-f", "concat", "-i",
            videoListFile.getAbsolutePath(), "-c", "copy",
            resultFile.getAbsolutePath()};


        Process proc = Runtime.getRuntime().exec(ffmpegCmd);
        proc.waitFor();
//delete files and conf
        videoListFile.delete();
        for (File toDelete : files) {
            toDelete.delete();
        }
        return resultFile;
    }
}
