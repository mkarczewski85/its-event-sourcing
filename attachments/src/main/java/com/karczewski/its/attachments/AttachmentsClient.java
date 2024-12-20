package com.karczewski.its.attachments;

import com.karczewski.its.attachments.dto.AttachmentsUploadDto;

public interface AttachmentsClient {

    void saveAttachments(AttachmentsUploadDto dto);

}
