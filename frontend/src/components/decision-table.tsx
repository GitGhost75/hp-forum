import { SurveyViewModel } from "@/lib/types";
import { formatCurrency, formatDate } from "@/lib/utils";

interface DecisionTableProps {
  surveys: SurveyViewModel[];
}

export function DecisionTable({ surveys }: DecisionTableProps) {
  return (
    <div className="overflow-x-auto rounded-2xl border border-slate-200 bg-white shadow-sm">
      <table className="min-w-full divide-y divide-slate-200 text-left text-sm text-slate-700">
        <thead className="bg-slate-100 text-xs uppercase tracking-wide text-slate-600">
          <tr>
            <th className="px-4 py-3">Referenz</th>
            <th className="px-4 py-3">Titel</th>
            <th className="px-4 py-3">Ergebnis</th>
            <th className="px-4 py-3">Entscheidungsschluessel</th>
            <th className="px-4 py-3">Budget</th>
            <th className="px-4 py-3">Laufzeit</th>
            <th className="px-4 py-3">Ruecklauf</th>
            <th className="px-4 py-3">Zustimmung</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100">
          {surveys.map((view) => {
            const positiveVotes = view.responseStats.positive + view.responseStats.partiallyPositive;
            return (
              <tr key={view.survey.id} className="hover:bg-slate-50">
                <td className="px-4 py-3 font-medium text-slate-900">
                  <div>{view.survey.surveyNumber}</div>
                  <div className="text-xs text-slate-500">
                    Aktiviert {formatDate(view.survey.activationDate)}
                  </div>
                </td>
                <td className="px-4 py-3">
                  <div className="font-semibold text-slate-900">{view.survey.name}</div>
                  <div className="text-xs text-slate-500">{view.topic.name}</div>
                </td>
                <td className="px-4 py-3 text-slate-800">
                  {view.survey.status === "completed" || view.survey.status === "archived"
                    ? "Dokumentiert"
                    : "In Auswertung"}
                </td>
                <td className="px-4 py-3 text-xs text-slate-600">
                  <div className="font-medium text-slate-700">
                    {view.decisionKey?.description ?? "Individuell"}
                  </div>
                  {view.decisionKey?.formula && (
                    <div>{view.decisionKey.formula}</div>
                  )}
                </td>
                <td className="px-4 py-3 text-xs text-slate-600">
                  <div>Einmalig: {formatCurrency(view.survey.totalAmountGross)}</div>
                  <div>Laufend: {formatCurrency(view.survey.recurringCostsGrossPa)}</div>
                </td>
                <td className="px-4 py-3 text-xs text-slate-600">
                  <div>Start: {formatDate(view.survey.activationDate)}</div>
                  <div>Ende: {formatDate(view.survey.closureDate ?? view.survey.responseDeadline)}</div>
                </td>
                <td className="px-4 py-3 text-xs text-slate-600">
                  {view.responseStats.submitted}/{view.responseStats.totalOrganizations}
                </td>
                <td className="px-4 py-3 text-xs text-slate-600">
                  {positiveVotes}/{view.responseStats.submitted}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
