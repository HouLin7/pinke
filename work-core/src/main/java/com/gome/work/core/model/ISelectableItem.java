package com.gome.work.core.model;

import java.io.Serializable;

public interface ISelectableItem extends Serializable {

    String getItemId();

    String getItemAvatar();

    String getItemName();

}
