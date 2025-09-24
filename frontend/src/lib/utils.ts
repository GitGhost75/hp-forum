import {
  IndicatorSet,
  MockDataBundle,
  ResponseStats,
  ResponseStatusCode,
  ResponseTypeCode,
  ResponseWithRelations,
  Survey,
  SurveyStatusCode,
  SurveyViewModel,
} from "./types";

type StatusColor = {
  bg: string;
  text: string;
  dot: string;
};

const statusColorMap: Record<SurveyStatusCode, StatusColor> = {
  active: {
    bg: "bg-emerald-100",
    text: "text-emerald-700",
    dot: "bg-emerald-500",
  },
  active_extended: {
    bg: "bg-amber-100",
    text: "text-amber-700",
    dot: "bg-amber-500",
  },
  active_overdue: {
    bg: "bg-rose-100",
    text: "text-rose-700",
    dot: "bg-rose-600",
  },
  draft: {
    bg: "bg-slate-100",
    text: "text-slate-700",
    dot: "bg-slate-400",
  },
  closed: {
    bg: "bg-blue-100",
    text: "text-blue-700",
    dot: "bg-blue-500",
  },
  completed: {
    bg: "bg-indigo-100",
    text: "text-indigo-700",
    dot: "bg-indigo-500",
  },
  cancelled: {
    bg: "bg-zinc-100",
    text: "text-zinc-700",
    dot: "bg-zinc-500",
  },
  archived: {
    bg: "bg-purple-100",
    text: "text-purple-700",
    dot: "bg-purple-500",
  },
};

export function getStatusColors(status: SurveyStatusCode): StatusColor {
  return statusColorMap[status];
}

export function formatCurrency(amount?: number, currency = "EUR"): string {
  if (amount === undefined || amount === null) {
    return "-";
  }

  return new Intl.NumberFormat("de-AT", {
    style: "currency",
    currency,
    maximumFractionDigits: amount % 1 === 0 ? 0 : 2,
  }).format(amount);
}

export function formatDate(value?: string): string {
  if (!value) {
    return "-";
  }

  const parsed = new Date(value);
  if (Number.isNaN(parsed.getTime())) {
    return value;
  }

  return parsed.toLocaleDateString("de-AT", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
}

function createLookup<T extends { id: number }>(items: T[]): Map<number, T> {
  return new Map(items.map((item) => [item.id, item]));
}

function isSubmitted(response: ResponseWithRelations): boolean {
  return response.responseStatus.code === "submitted" || response.responseStatus.code === "released";
}

export function calculateResponseStats(
  totalOrganizations: number,
  responses: ResponseWithRelations[],
): ResponseStats {
  const submitted = responses.filter(isSubmitted);
  const positive = submitted.filter((response) => response.responseType?.code === "OK").length;
  const partiallyPositive = submitted.filter((response) => response.responseType?.code === "BOK").length;
  const negative = submitted.filter((response) => response.responseType?.code === "NOK").length;

  return {
    totalOrganizations,
    submitted: submitted.length,
    positive,
    partiallyPositive,
    negative,
  };
}

function ratio(numerator: number, denominator: number): number {
  if (denominator === 0) {
    return 0;
  }

  return numerator / denominator;
}

function getOverdueDays(survey: Survey): number {
  if (!survey.responseDeadline) {
    return 0;
  }

  const due = new Date(survey.responseDeadline);
  const reference = survey.closureDate
    ? new Date(survey.closureDate)
    : survey.extendedDeadline
      ? new Date(survey.extendedDeadline)
      : new Date();

  if (Number.isNaN(due.getTime()) || Number.isNaN(reference.getTime())) {
    return 0;
  }

  const diff = reference.getTime() - due.getTime();
  const days = Math.floor(diff / (1000 * 60 * 60 * 24));
  return days > 0 ? days : 0;
}

function getExtensionCount(survey: Survey): number {
  if (!survey.extendedDeadline || !survey.responseDeadline) {
    return 0;
  }

  const extended = new Date(survey.extendedDeadline);
  const original = new Date(survey.responseDeadline);
  if (Number.isNaN(extended.getTime()) || Number.isNaN(original.getTime())) {
    return 0;
  }

  return extended > original ? 1 : 0;
}

export function calculateIndicators(survey: Survey, stats: ResponseStats): IndicatorSet {
  const approvalBase = stats.submitted;
  const positiveLike = stats.positive + stats.partiallyPositive;

  return {
    responseRatio: ratio(stats.submitted, stats.totalOrganizations),
    approvalRatio: ratio(positiveLike, approvalBase),
    overdueDays: getOverdueDays(survey),
    extensionCount: getExtensionCount(survey),
  };
}

export function buildSurveyViewModels(data: MockDataBundle): SurveyViewModel[] {
  const organizationMap = createLookup(data.organizations);
  const userMap = createLookup(data.users);
  const topicMap = createLookup(data.topics);
  const topicCategoryMap = createLookup(data.topicCategories);
  const topicStatusMap = createLookup(data.topicStatuses);
  const categoryMap = createLookup(data.surveyCategories);
  const decisionKeyMap = createLookup(data.decisionKeys);
  const responseTypeMap = createLookup(data.responseTypes);
  const responseStatusMap = createLookup(data.responseStatuses);
  const attachmentMap = createLookup(data.attachments);

  return data.surveys.map((survey) => {
    const organizations = data.surveyOrganizations
      .filter((relation) => relation.surveyId === survey.id)
      .map((relation) => organizationMap.get(relation.organizationId))
      .filter((organization): organization is Exclude<typeof organization, undefined> => Boolean(organization));

    const responses: ResponseWithRelations[] = data.responses
      .filter((response) => response.surveyId === survey.id)
      .map((response) => {
        const organization = organizationMap.get(response.organizationId);
        const user = userMap.get(response.userId);
        const delegatedForUser = response.delegatedForUserId
          ? userMap.get(response.delegatedForUserId)
          : undefined;
        const responseStatus = responseStatusMap.get(response.responseStatusId);
        const responseType = response.responseTypeId
          ? responseTypeMap.get(response.responseTypeId)
          : undefined;

        if (!organization || !user || !responseStatus) {
          throw new Error("Mock data integrity issue in responses");
        }

        return {
          ...response,
          organization,
          user,
          delegatedForUser,
          responseStatus,
          responseType,
        };
      });

    const attachments = data.surveyAttachments
      .filter((relation) => relation.surveyId === survey.id)
      .map((relation) => attachmentMap.get(relation.attachmentId))
      .filter((attachment): attachment is Exclude<typeof attachment, undefined> => Boolean(attachment));

    const topic = topicMap.get(survey.topicId);
    const topicCategory = topic ? topicCategoryMap.get(topic.topicCategoryId) : undefined;
    const topicStatus = topic ? topicStatusMap.get(topic.topicStatusId) : undefined;
    const category = categoryMap.get(survey.surveyCategoryId);
    const decisionKey = survey.decisionKeyId ? decisionKeyMap.get(survey.decisionKeyId) : undefined;
    const createdBy = userMap.get(survey.createdById);

    if (!topic || !topicCategory || !topicStatus || !category || !createdBy) {
      throw new Error("Mock data integrity issue in survey relations");
    }

    const stats = calculateResponseStats(organizations.length, responses);
    const indicators = calculateIndicators(survey, stats);

    return {
      survey,
      category,
      decisionKey,
      topic,
      topicCategory,
      topicStatus,
      createdBy,
      organizations,
      responses,
      attachments,
      responseStats: stats,
      indicators,
    };
  });
}

export function getResponseIndicatorColor(ratioValue: number) {
  if (ratioValue >= 1) return { text: "text-emerald-700", bg: "bg-emerald-100" };
  if (ratioValue >= 0.75) return { text: "text-emerald-700", bg: "bg-emerald-50" };
  if (ratioValue >= 0.25) return { text: "text-amber-700", bg: "bg-amber-50" };
  return { text: "text-rose-700", bg: "bg-rose-50" };
}

export function getApprovalIndicatorColor(ratioValue: number) {
  if (ratioValue >= 0.75) return { text: "text-emerald-700", bg: "bg-emerald-50" };
  if (ratioValue >= 0.5) return { text: "text-lime-700", bg: "bg-lime-50" };
  if (ratioValue >= 0.25) return { text: "text-amber-700", bg: "bg-amber-50" };
  return { text: "text-rose-700", bg: "bg-rose-50" };
}

export function decisionToLabel(code?: ResponseTypeCode) {
  switch (code) {
    case "OK":
      return "OK";
    case "BOK":
      return "Bedingt OK";
    case "NOK":
      return "Nicht OK";
    case "INFO":
      return "Info";
    default:
      return "-";
  }
}

export function workflowStateToLabel(code: ResponseStatusCode) {
  switch (code) {
    case "released":
      return "freigegeben";
    case "submitted":
      return "eingereicht";
    default:
      return "Entwurf";
  }
}

export function findDecisionKeyByCode(
  decisionKeys: { id: number; code: string }[],
  code: string | undefined,
) {
  if (!code) return undefined;
  return decisionKeys.find((item) => item.code === code);
}

export function generateSurveyNumber(surveys: Survey[], now: Date = new Date()): string {
  const year = now.getFullYear();
  const prefix = `${year}_`;
  const nextNumber = surveys
    .filter((survey) => survey.surveyNumber.startsWith(prefix))
    .map((survey) => Number.parseInt(survey.surveyNumber.slice(prefix.length), 10))
    .filter((value) => Number.isFinite(value))
    .reduce((max, value) => Math.max(max, value), 0) + 1;

  return `${year}_${String(nextNumber).padStart(4, "0")}`;
}


