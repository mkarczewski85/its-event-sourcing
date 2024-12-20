package com.karczewski.its.attachments.service;

import com.karczewski.its.attachments.AttachmentsClient;
import com.karczewski.its.attachments.dto.AttachmentsUploadDto;
import com.karczewski.its.attachments.entity.AttachmentUpload;
import com.karczewski.its.attachments.respository.AttachmentUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttachmentsService implements AttachmentsClient {

    private final AttachmentUploadRepository attachmentUploadRepository;

    @Override
    @Transactional
    public void saveAttachments(AttachmentsUploadDto dto) {
        AttachmentUpload attachmentUpload = AttachmentUpload.builder()
                .issueId(dto.issueId())
                .name(dto.filename())
                .contentType(dto.contentType())
                .data(dto.data())
                .build();
        attachmentUploadRepository.save(attachmentUpload);
    }
}
