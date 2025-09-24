"use client";

import { SURVEY_STATUS_LABELS, SurveyStatusCode } from "@/lib/types";
import { getStatusColors } from "@/lib/utils";

interface StatusBadgeProps {
  status: SurveyStatusCode;
}

export function StatusBadge({ status }: StatusBadgeProps) {
  const colors = getStatusColors(status);

  return (
    <span
      className={`inline-flex items-center gap-1.5 rounded-full px-3 py-1 text-xs font-medium ${colors.bg} ${colors.text}`}
    >
      <span className={`h-2 w-2 rounded-full ${colors.dot}`} />
      {SURVEY_STATUS_LABELS[status]}
    </span>
  );
}
