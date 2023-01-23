package com.lehit.programs.model.payload;

import com.lehit.common.enums.Language;
import com.lehit.programs.model.enums.ActionItemType;

import java.util.UUID;

public record ExecutedItemRequest(UUID taskExecutionId, int duration, String reply, byte[] drawing, Language language, ActionItemType itemType){}
