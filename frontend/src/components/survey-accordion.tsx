import { SurveyViewModel } from "@/lib/types";
import { formatDate } from "@/lib/utils";
import { CategoryBadge } from "./category-badge";
import { IndicatorPill } from "./indicator-pill";
import { StatusBadge } from "./status-badge";
import { SurveyCard } from "./survey-card";

interface SurveyAccordionProps {
  items: SurveyViewModel[];
  focusOrganizationId?: number;
}

export function SurveyAccordion({ items, focusOrganizationId }: SurveyAccordionProps) {
  if (items.length === 0) {
    return null;
  }

  return (
    <div className="space-y-3">
      {items.map((view) => {
        const positiveVotes = view.responseStats.positive + view.responseStats.partiallyPositive;
        return (
          <details
            key={view.survey.id}
            className="group rounded-2xl border border-slate-200 bg-white transition-shadow hover:shadow-sm"
          >
            <summary className="flex cursor-pointer list-none items-center justify-between gap-4 px-5 py-4">
              <div className="flex flex-wrap items-center gap-2">
                <StatusBadge status={view.survey.status} />
                <CategoryBadge category={view.category} />
                <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-medium text-slate-600">
                  {view.survey.surveyNumber}
                </span>
                <span className="text-sm font-semibold text-slate-900">{view.survey.name}</span>
              </div>
              <div className="flex items-center gap-2 text-right text-xs text-slate-500">
                <IndicatorPill
                  type="response"
                  numerator={view.responseStats.submitted}
                  denominator={view.responseStats.totalOrganizations}
                />
                <IndicatorPill
                  type="approval"
                  numerator={positiveVotes}
                  denominator={view.responseStats.submitted}
                />
                <span className="hidden sm:block">Frist: {formatDate(view.survey.responseDeadline)}</span>
                <span className="transition-transform group-open:rotate-180">^</span>
              </div>
            </summary>
            <div className="border-t border-slate-200 px-5 pb-5 pt-4">
              <SurveyCard view={view} focusOrganizationId={focusOrganizationId} />
            </div>
          </details>
        );
      })}
    </div>
  );
}
