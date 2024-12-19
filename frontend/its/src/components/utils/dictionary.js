import {value} from "lodash/seq.js";

const severityOptions = [
  {title: 'Niski', value: 'LOW'},
  {title: 'Średni', value: 'MEDIUM'},
  {title: 'Wysoki', value: 'HIGH'},
];

const statusOptions = [
  {title: 'Utworzone', value: 'REPORTED'},
  {title: 'Przypisane', value: 'ASSIGNED'},
  {title: 'Realizowane', value: 'IN_PROGRESS'},
  {title: 'Odrzucone', value: 'REJECTED'},
  {title: 'Anulowane', value: 'CANCELLED'},
  {title: 'Rozwiązane', value: 'RESOLVED'}
];

const typeOptions = [
  {title: 'Problem', value: 'PROBLEM'},
  {title: 'Incydent', value: 'INCIDENT'},
  {title: 'Wniosek o zmianę', value: 'CHANGE_REQUEST'},
  {title: 'Usługa serwisowa', value: 'SERVICE_REQUEST'}
];

const userRoleOptions = [
  {title: 'Technik', value: 'TECHNICIAN'},
  {title: 'Reporter', value: 'REPORTER'},
  {title: 'Administrator', value: 'ADMINISTRATOR'}
];

export const getStatusOptionByValue = (value) => {
  return statusOptions.find(option => option.value === value);
};

const getTitleByValue = (value, options) => {
  const option = options.find(option => option.value === value);
  return option ? option.title : '-';
};

const getStatusTitleByValue = (value) => {
  return getTitleByValue(value, statusOptions);
};

const getSeverityTitleByValue = (value) => {
  return getTitleByValue(value, severityOptions);
};

const getTypeTitleByValue = (value) => {
  return getTitleByValue(value, typeOptions);
};

export default {
  severityOptions,
  statusOptions,
  typeOptions,
  userRoleOptions,
  getTitleByValue,
  getStatusOptionByValue,
  getStatusTitleByValue,
  getSeverityTitleByValue,
  getTypeTitleByValue
}
