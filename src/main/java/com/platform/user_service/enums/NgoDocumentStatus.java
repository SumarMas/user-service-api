package com.platform.user_service.enums;
/**
 * Enum representing the status of NGO documents.
 */
public enum NgoDocumentStatus {
    /**
     * Status when the document has been uploaded but not yet reviewed.
     */
    RECEIVED,
    /**
     * Status when the document has been reviewed and approved.
     */
    ACCEPTED,
    /**
     * Status when the document has been reviewed and rejected.
     */
    REJECTED
}
