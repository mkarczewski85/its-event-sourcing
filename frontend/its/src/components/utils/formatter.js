const formatSeverityChipColor = (severity) => {
    const severityColors = {
        HIGH: 'error',
        MEDIUM: 'warning',
        LOW: 'success'
    };
    return severityColors[severity] || 'default'; // 'default' as fallback
};

export const formatDate = (date) => {
    const options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    };
    return new Date(date).toLocaleDateString('pl-PL', options);
}

export default {
    formatSeverityChipColor,
    formatDate
}
