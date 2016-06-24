package com.deadpineapple.transcoder;

/**
 * Created by 15256 on 12/02/2016.
 */
public class MainTranscoder {


    public static void main(String[] args) {

        ConversionLauncher launcher;
        if (args.length > 0) {
            launcher = new ConversionLauncher(args[0]);
        }else {
            launcher = new ConversionLauncher();
        }
        launcher.start();
    }
}
