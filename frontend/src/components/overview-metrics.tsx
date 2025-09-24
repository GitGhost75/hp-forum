import { SurveyViewModel } from "@/lib/types";

interface OverviewMetricsProps {
  surveys: SurveyViewModel[];
}

const metricConfig = [
    {
    id: "all",
    title: "Gesamt Umfragen",
    helper: (surveys: SurveyViewModel[]) =>surveys.length,
    description: "Umfragen insgesamt",
  },
  {
    id: "drafts",
    title: "Entwuerfe",
    helper: (surveys: SurveyViewModel[]) =>
      surveys.filter((view) => view.survey.status === "draft").length,
    description: "Umfragen in Vorbereitung",
  },
  {
    id: "active",
    title: "Aktive Umfragen",
    helper: (surveys: SurveyViewModel[]) =>
      surveys.filter((view) =>
        ["active", "active_extended"].includes(view.survey.status),
      ).length,
    description: "Laufende Abstimmungen",
  },
    {
    id: "active_overdue",
    title: "Überfällige Umfragen",
    helper: (surveys: SurveyViewModel[]) =>
      surveys.filter((view) =>
        ["active_overdue"].includes(view.survey.status),
      ).length,
    description: "Überfällige Abstimmungen",
  },
  {
    id: "pending",
    title: "Offene Rueckmeldungen",
    helper: (surveys: SurveyViewModel[]) =>
      surveys.reduce((accumulator, view) => {
        const pending =
          view.responseStats.totalOrganizations - view.responseStats.submitted;
        return accumulator + (pending > 0 ? pending : 0);
      }, 0),
    description: "Benötigte Organisationen in aktiven Umfragen",
  },
  {
    id: "recent",
    title: "Gerade abgeschlossen",
    helper: (surveys: SurveyViewModel[]) =>
      surveys.filter((view) => ["closed", "completed"].includes(view.survey.status)).length,
    description: "Kürzlich beendete Umfragen",
  },
  {
    id: "ok",
    title: "Zustimmungsrate",
    helper: (surveys: SurveyViewModel[]) => 0,
    description: "Verhältnis positiver Abstimmungen",
  },
];

export function OverviewMetrics({ surveys }: OverviewMetricsProps) {
  return (
    <section className="grid mt-5 gap-4 md:grid-cols-2 xl:grid-cols-4">
      {metricConfig.map((metric) => (
        <article
          key={metric.id}
          className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm"
        >
          <p className="text-xs font-medium uppercase tracking-wide text-slate-500">
            {metric.title}
          </p>
          <p className="mt-2 text-3xl font-semibold text-slate-900">
            {metric.helper(surveys)}
          </p>
          <p className="mt-1 text-sm text-slate-600">{metric.description}</p>
        </article>
      ))}
    </section>
  );
}
