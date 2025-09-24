import { SurveyViewModel } from "@/lib/types";
import {
  decisionToLabel,
  formatCurrency,
  formatDate,
  workflowStateToLabel,
} from "@/lib/utils";

interface SurveyCardProps {
  view: SurveyViewModel;
  focusOrganizationId?: number | null;
}

const isFinalResponse = (statusCode: string) =>
  statusCode === "submitted" || statusCode === "released";

export function SurveyCard({ view, focusOrganizationId }: SurveyCardProps) {
  const selectedResponse = focusOrganizationId
    ? view.responses.find((response) => response.organization.id === focusOrganizationId)
    : undefined;
  const pendingOrganizations = view.organizations.filter((organization) => {
    const response = view.responses.find((item) => item.organization.id === organization.id);
    return !response || !isFinalResponse(response.responseStatus.code);
  });

  return (
    // <article className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition-all hover:border-sky-200 hover:shadow-md">
    <article>

      <header className="mt--2 flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
        <div>
              <p className="mt-1 text-sm text-slate-600">{view.survey.shortDescription}</p>
          <div className="mt-3 flex flex-wrap items-center gap-2 text-xs font-medium text-slate-500">
            <span className="rounded-full bg-slate-100 px-2.5 py-1">
              Thema: {view.topic.name}
            </span>
            <span className="rounded-full bg-slate-100 px-2.5 py-1">
              Kategorie: {view.topicCategory.name}
            </span>
            <span className="rounded-full bg-slate-100 px-2.5 py-1">
              Status: {view.topicStatus.name}
            </span>
          </div>
        </div>
        {focusOrganizationId && selectedResponse && (
          <div className="rounded-xl border border-sky-100 bg-sky-50 px-4 py-3 text-sm text-sky-800">
            <p className="font-semibold">
              Status fuer {selectedResponse.organization.shortName}
            </p>
            <p className="mt-1">
              Workflow: {workflowStateToLabel(selectedResponse.responseStatus.code)}
            </p>
            <p>
              Antwort: {decisionToLabel(selectedResponse.responseType?.code)}
            </p>
          </div>
        )}
      </header>

      <dl className="mt-6 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <InfoItem label="Erstellt am" value={formatDate(view.survey.createdAt)} />
        <InfoItem label="Aktiv seit" value={formatDate(view.survey.activationDate)} />
        <InfoItem
          label="Rueckmeldefrist"
          value={formatDate(view.survey.extendedDeadline ?? view.survey.responseDeadline)}
        />
        <InfoItem
          label="Ueberziehungstage"
          value={view.indicators.overdueDays > 0 ? `${view.indicators.overdueDays} Tage` : "-"}
        />
        <InfoItem
          label="Gesamtbudget"
          value={formatCurrency(view.survey.totalAmountGross)}
        />
        <InfoItem
          label="Laufende Kosten p.a."
          value={formatCurrency(view.survey.recurringCostsGrossPa)}
        />
        <InfoItem
          label="Personentage"
          value={view.survey.personDays ? `${view.survey.personDays} PT` : "-"}
        />
        <InfoItem
          label="Tagsatz"
          value={formatCurrency(view.survey.dailyRateGross)}
        />
      </dl>

      {(view.decisionKey || view.survey.decisionKeyId) && (
        <div className="mt-6 rounded-xl border border-slate-200 bg-slate-50 p-4 text-sm text-slate-700">
          <p className="font-semibold text-slate-800">Entscheidungsschluessel</p>
          <p className="mt-1">{view.decisionKey?.description ?? "Individuelle Regelung"}</p>
          {view.decisionKey?.formula && (
            <p className="mt-1 text-slate-600">Formel: {view.decisionKey.formula}</p>
          )}
        </div>
      )}

      <section className="mt-6">
        <h3 className="text-sm font-semibold text-slate-800">Rueckmeldungen</h3>
        <div className="mt-3 grid gap-2">
          {view.responses.map((response) => {
            const isFocus = focusOrganizationId === response.organization.id;
            return (
              <div
                key={`${view.survey.id}-${response.organization.id}`}
                className={`flex flex-wrap items-center justify-between gap-3 rounded-lg border border-slate-200 px-3 py-2 text-sm ${
                  isFocus ? "border-sky-300 bg-sky-50" : "bg-white"
                }`}
              >
                <div className="flex flex-wrap items-center gap-2">
                  <span className="rounded bg-slate-100 px-2 py-0.5 text-xs font-medium text-slate-600">
                    {response.organization.shortName}
                  </span>
                  <span className="text-slate-700">
                    {decisionToLabel(response.responseType?.code)}
                  </span>
                  <span className="text-xs text-slate-500">
                    {workflowStateToLabel(response.responseStatus.code)}
                  </span>
                  {response.delegatedForUser && (
                    <span className="text-xs text-slate-500">
                      in Vertretung fuer {response.delegatedForUser.name}
                    </span>
                  )}
                </div>
                <div className="text-xs text-slate-500">
                  Aktualisiert am {formatDate(response.updatedAt)}
                </div>
              </div>
            );
          })}
        </div>
        {pendingOrganizations.length > 0 && (
          <p className="mt-3 text-xs text-slate-500">
            Offen fuer: {pendingOrganizations.map((org) => org.shortName).join(", ")}
          </p>
        )}
      </section>

      {view.attachments.length > 0 && (
        <section className="mt-6 rounded-xl border border-slate-200 bg-slate-50 p-4 text-sm text-slate-700">
          <p className="font-semibold text-slate-800">Anhaenge</p>
          <ul className="mt-2 list-disc pl-5">
            {view.attachments.map((attachment) => (
              <li key={attachment.id}>
                {attachment.filename} ({Math.round(attachment.filesize / 1024)} kB)
              </li>
            ))}
          </ul>
        </section>
      )}
    </article>
  );
}

function InfoItem({ label, value }: { label: string; value: string }) {
  return (
    <div className="rounded-lg border border-slate-100 bg-slate-50 px-3 py-2">
      <dt className="text-xs font-medium uppercase tracking-wide text-slate-500">
        {label}
      </dt>
      <dd className="mt-1 text-sm text-slate-700">{value}</dd>
    </div>
  );
}



