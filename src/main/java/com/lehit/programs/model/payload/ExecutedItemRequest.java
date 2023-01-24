package com.lehit.programs.model.payload;

import com.lehit.programs.model.enums.ActionItemType;

import java.util.UUID;

public record ExecutedItemRequest(UUID taskExecutionId, String reply, byte[] drawing, ActionItemType itemType){}
