export interface OrganizationCategory {
  id: number;
  name: string;
  description?: string;
}

export interface Organization {
  id: number;
  name: string;
  shortName: string;
  acnClientNumber: string;
  organizationCategoryId: number;
}

export interface Role {
  id: number;
  name: string;
  description?: string;
}

export interface User {
  id: number;
  name: string;
  email: string;
  organizationId: number;
  roleId: number;
}

export interface TopicCategory {
  id: number;
  name: string;
  description?: string;
}

export interface TopicStatus {
  id: number;
  name: string;
  description?: string;
}

export interface Topic {
  id: number;
  name: string;
  shortDescription: string;
  longDescription?: string;
  topicCategoryId: number;
  topicStatusId: number;
  responsibleUserId: number;
  primaryContactId: number;
}

export interface SurveyCategory {
  id: number;
  code: string;
  name: string;
  description?: string;
}

export interface DecisionKey {
  id: number;
  code: string;
  description: string;
  formula?: string;
}

export interface CategoryDecisionKey {
  surveyCategoryId: number;
  decisionKeyId: number;
}

export type SurveyStatusCode =
  | "draft"
  | "active"
  | "active_extended"
  | "active_overdue"
  | "closed"
  | "completed"
  | "cancelled"
  | "archived";

export interface Survey {
  id: number;
  surveyNumber: string;
  name: string;
  shortDescription: string;
  longDescription?: string;
  status: SurveyStatusCode;
  responseDeadline?: string;
  createdById: number;
  topicId: number;
  surveyCategoryId: number;
  decisionKeyId?: number;
  totalAmountGross?: number;
  personDays?: number;
  dailyRateGross?: number;
  recurringCostsGrossPa?: number;
  activationDate?: string;
  closureDate?: string;
  archiveDate?: string;
  extendedDeadline?: string;
  createdAt?: string;
}

export interface SurveyOrganization {
  surveyId: number;
  organizationId: number;
}

export type ResponseTypeCode = "OK" | "BOK" | "NOK" | "INFO";

export interface ResponseType {
  id: number;
  code: ResponseTypeCode;
  description: string;
}

export type ResponseStatusCode = "draft" | "released" | "submitted";

export interface ResponseStatus {
  id: number;
  name: string;
  code: ResponseStatusCode;
  description?: string;
}

export interface Response {
  id: number;
  surveyId: number;
  organizationId: number;
  userId: number;
  delegatedForUserId?: number;
  responseStatusId: number;
  responseTypeId?: number;
  responseJustification?: string;
  delegationJustification?: string;
  updatedAt?: string;
}

export interface Attachment {
  id: number;
  filename: string;
  filetype: string;
  filesize: number;
  data?: string;
}

export interface SurveyAttachment {
  surveyId: number;
  attachmentId: number;
}

export interface ResponseAttachment {
  responseId: number;
  attachmentId: number;
}

export interface AuditLog {
  id: number;
  entityName: string;
  entityId: number;
  changedBy: number;
  timestamp: string;
  changes: string;
}

export interface ResponseStats {
  totalOrganizations: number;
  submitted: number;
  positive: number;
  partiallyPositive: number;
  negative: number;
}

export interface IndicatorSet {
  responseRatio: number;
  approvalRatio: number;
  overdueDays: number;
  extensionCount: number;
}

export interface ResponseWithRelations extends Response {
  organization: Organization;
  user: User;
  delegatedForUser?: User;
  responseStatus: ResponseStatus;
  responseType?: ResponseType;
}

export interface SurveyViewModel {
  survey: Survey;
  category: SurveyCategory;
  decisionKey?: DecisionKey;
  topic: Topic;
  topicCategory: TopicCategory;
  topicStatus: TopicStatus;
  createdBy: User;
  organizations: Organization[];
  responses: ResponseWithRelations[];
  attachments: Attachment[];
  responseStats: ResponseStats;
  indicators: IndicatorSet;
}

export interface MockDataBundle {
  organizationCategories: OrganizationCategory[];
  organizations: Organization[];
  roles: Role[];
  users: User[];
  topicCategories: TopicCategory[];
  topicStatuses: TopicStatus[];
  topics: Topic[];
  surveyCategories: SurveyCategory[];
  decisionKeys: DecisionKey[];
  categoryDecisionKeys: CategoryDecisionKey[];
  surveys: Survey[];
  surveyOrganizations: SurveyOrganization[];
  responseTypes: ResponseType[];
  responseStatuses: ResponseStatus[];
  responses: Response[];
  attachments: Attachment[];
  surveyAttachments: SurveyAttachment[];
  responseAttachments: ResponseAttachment[];
  auditLogs: AuditLog[];
}

export const SURVEY_STATUS_LABELS: Record<SurveyStatusCode, string> = {
  draft: "Entwurf",
  active: "Aktiv",
  active_extended: "Aktiv (verlaengert)",
  active_overdue: "Aktiv (ueberzogen)",
  closed: "Beendet",
  completed: "Abgeschlossen",
  cancelled: "Abgebrochen",
  archived: "Archiviert",
};
