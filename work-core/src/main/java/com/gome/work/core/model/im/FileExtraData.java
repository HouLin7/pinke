package com.gome.work.core.model.im;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Create by liupeiquan on 2019/3/21
 */
public class FileExtraData implements Serializable {
    private static final long serialVersionUID = 7948109070329797930L;
    public List<File> uriList;
    public ShareFileType fileType;

    public enum ShareFileType {
        FILE,
        IMAGE
    }


}
