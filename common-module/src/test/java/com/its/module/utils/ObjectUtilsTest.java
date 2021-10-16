package com.its.module.utils;

import com.its.module.model.entity.Editable;
import com.its.module.model.entity.Modifiable;
import com.its.module.model.entity.Removable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilsTest {
    @Test
    void setCreateHistory1() {
        Editable editable = Mockito.mock(Editable.class);
        Assertions.assertDoesNotThrow(() -> ObjectUtils.setCreateHistory(editable, 7));
    }

    @Test
    void setCreateHistory2() {
        Removable editable = Mockito.mock(Removable.class);
        Assertions.assertDoesNotThrow(() -> ObjectUtils.setCreateHistory(editable, 7));
    }

    @Test
    void setModifyHistory() {
        Modifiable modifiable = Mockito.mock(Modifiable.class);
        Assertions.assertDoesNotThrow(() -> ObjectUtils.setModifyHistory(modifiable, 7));
    }
}