package com.karczewski.its.attachments.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AttachmentsUploadDto(
        UUID issueId,
        String filename,
        String contentType,
        byte[] data
) {
}
