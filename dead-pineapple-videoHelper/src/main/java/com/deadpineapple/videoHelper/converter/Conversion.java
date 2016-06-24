package com.deadpineapple.videoHelper.converter;

import com.deadpineapple.dal.constante.Constante;
import com.deadpineapple.videoHelper.FfmpegException;

import java.io.IOException;
import java.util.*;

/**
 * Created by 15256 on 11/03/2016.
 */
public class Conversion {


    private String fileCodec;
    private Process process;
    private String filePath;
    private String fileDestinationPath;
    private String fileType;
    private Dictionary<String, String> outputFileOptionDictionnary;

    public Conversion() {

    }

    public Conversion(String filePath, String fileDestinationPath, String fileType, String encoding) {
        this.filePath = filePath;
        this.fileDestinationPath = fileDestinationPath;
        this.fileType = fileType;
        this.fileCodec = encoding;
        this.outputFileOptionDictionnary = getOutputOptionDictionnary();
    }


    public Boolean start() throws IOException, InterruptedException, FfmpegException {
        process = Runtime.getRuntime().exec(generateFfmpegCommand());
        return process.waitFor() == 0;
    }

    private String[] generateFfmpegCommand() throws FfmpegException {
        if (!Arrays.asList(Constante.AcceptedConversionTypes).contains(fileType)) {
            throw new FfmpegException("Unvalid type");
        }
        if (!Arrays.asList(Constante.AcceptedCodec).contains(fileCodec)){
            throw new FfmpegException("Unvalid encoding");
        }

        String outputFileOptions = "-threads 2";
        if (outputFileOptionDictionnary.get(fileType) != null) {
            outputFileOptions += outputFileOptionDictionnary.get(fileType);
        }
        /*
        String cmd = "ffmpeg";
        cmd += globalOptions;
        cmd += inputFileOptions;
        cmd += "-i \"" + getFilePath() + "\"";
        cmd += getEncodingOutput();
        cmd += outputFileOptions;
        cmd += "\"" + getFileDestinationPath() + "\"";
        */

        List<String> cmd = new ArrayList<String>();
        cmd.add("ffmpeg");
        cmd.add("-y");
        cmd.add("-nostats");
        cmd.add("-loglevel");
        cmd.add("0");
        cmd.add("-i");
        cmd.add(getFilePath());
        cmd.addAll(Arrays.asList(getEncodingOutput().split(" ")));
        cmd.addAll(Arrays.asList(outputFileOptions.split(" ")));
        cmd.add(getFileDestinationPath());
        String[] res = new String[cmd.size()];
        return cmd.toArray(res);
    }

    public Process getProcess() {
        return process;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileDestinationPath() {
        return fileDestinationPath;
    }

    public void setFileDestinationPath(String fileDestinationPath) {
        this.fileDestinationPath = fileDestinationPath;
    }

    Dictionary<String, String> getOutputOptionDictionnary() {
        Dictionary<String, String> dico = new Hashtable<String, String>();
        dico.put(".flv", "-ar 22050 -crf 28 ");
        dico.put(".swf", "-ar 22050 -crf 28 ");
        dico.put(".dv", "-target pal-dv ");
        return dico;
    }

    String getEncodingOutput() {
        if (fileCodec.equals("ffv1")) {
            return " -vcodec ffv1 -level 3";
        } else if (fileCodec.equals("h.264")) {
            return " -c:v libx264 -preset ultrafast -qp 0";
        } else if (fileCodec.equals("vp8")) {
            return " -c:v libvpx -b:v 1M -c:a libvorbis";
        } else if (fileCodec.equals("vp9")) {
            return " -c:v libvpx-vp9 -b:v 1M -c:a libvorbis";
        }else if (fileCodec.equals("xvid")) {
            return " -c:v mpeg4 -vtag xvid";
        }
        return "";
    }

}
