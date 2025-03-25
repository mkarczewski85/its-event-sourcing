package com.karczewski.its.attachments.respository;

import com.karczewski.its.attachments.entity.AttachmentUpload;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentUploadRepository extends CrudRepository<AttachmentUpload, Long> {
}
