import { getStatusOptionByValue }  from "./dictionary.js"

const resolveAllowedNextStatuses = (status) => {
    const inProgressOption = getStatusOptionByValue('IN_PROGRESS');
    const rejectOption = getStatusOptionByValue('REJECTED');
    const cancelOption = getStatusOptionByValue('CANCELLED');
    const resolveOption = getStatusOptionByValue('RESOLVED');
    const allowedNextStatuses = {
        'ASSIGNED': [rejectOption, inProgressOption],
        'IN_PROGRESS': [cancelOption, resolveOption]
    };
    return allowedNextStatuses[status];
}

export default {
    resolveAllowedNextStatuses
}
