"use client";

import { useEffect, useMemo, useState, type ReactNode } from "react";

import { CreateSurveyForm, CreateSurveyFormValues } from "@/components/create-survey-form";
import { Activity, Archive, BarChart3, Clock3, FileEdit, Plus, Scale } from "lucide-react";
import Select, { type StylesConfig } from "react-select";
import { DecisionTable } from "@/components/decision-table";
import { OverviewMetrics } from "@/components/overview-metrics";
import { SurveyAccordion } from "@/components/survey-accordion";
import { LeftMenu } from "@/components/left-menu";
import { TopMenu } from "@/components/top-menu";
import { createMockDataBundle } from "@/lib/mock-data";
import {
  Survey,
  SurveyStatusCode,
  SurveyViewModel,
} from "@/lib/types";
import {
  buildSurveyViewModels,
  generateSurveyNumber,
  formatDate,
} from "@/lib/utils";

const ACTIVE_STATUSES: SurveyStatusCode[] = ["active", "active_extended", "active_overdue"];
const RECENT_STATUSES: SurveyStatusCode[] = ["closed", "completed"];
const DECISION_CATEGORY_CODES = new Set(["HPIT_DEC", "GEN_DEC"]);

const TAB_ORDER = [
  "dashboard",
  "drafts",
  // "active",
  "active-organization",
  "recent",
  "archive",
  "decision",
  "createSurvey",
] as const;

type TabKey = (typeof TAB_ORDER)[number];

type OrganizationOption = { value: string; label: string };

const TAB_LABELS: Record<TabKey, { title: string; description: string }> = {
  dashboard: {
    title: "Dashboard",
    description: "Uebersicht und Kennzahlen zu allen Umfragen.",
  },
  // active: {
  //   title: "Aktive Umfragen",
  //   description: "Ueberblick ueber laufende Abstimmungen inklusive Verlaengerungen.",
  // },
  "active-organization": {
    title: "Aktive Umfragen",
    description: "Gefilterte Sicht fuer eine gewaehlte Organisation mit Fokus auf offene Rueckmeldungen.",
  },
  drafts: {
    title: "Entwürfe",
    description: "Nur fuer HP IT sichtbar - Vorbereitung neuer Abstimmungen.",
  },
  recent: {
    title: "Beendete Umfragen",
    description: "Umfragen, die kuerzlich beendet oder ausgewertet wurden.",
  },
  archive: {
    title: "Archiv",
    description: "Historische Umfragen mit Dokumentation.",
  },
  decision: {
    title: "Beschlussspiegel",
    description: "Ausgewertete Beschlussantraege inklusive Kennzahlen und Kostenuebersicht.",
  },
  createSurvey: {
    title: "Neue Umfrage",
    description: "Erstellen Sie eine neue Umfrage fuer das HP-Forum.",
  },
};

const TAB_ICONS: Record<TabKey, () => ReactNode> = {
  dashboard: () => (
    <BarChart3 className="h-4 w-4" strokeWidth={1.75} aria-hidden="true" />
  ),
  // active: () => (
  //   <Activity className="h-4 w-4" strokeWidth={1.75} aria-hidden="true" />
  // ),
  "active-organization": () => (
    <Activity className="h-4 w-4" strokeWidth={1.75} aria-hidden="true" />
  ),
  drafts: () => (
    <FileEdit className="h-4 w-4" strokeWidth={1.75} aria-hidden="true" />
  ),
  recent: () => (
    <Clock3 className="h-4 w-4" strokeWidth={1.75} aria-hidden="true" />
  ),
  archive: () => (
    <Archive className="h-4 w-4" strokeWidth={1.75} aria-hidden="true" />
  ),
  decision: () => (
    <Scale className="h-4 w-4" strokeWidth={1.75} aria-hidden="true" />
  ),
  createSurvey: () => (
    <Plus className="h-4 w-4" strokeWidth={1.75} aria-hidden="true" />
  ),
};

const STATUS_FILTERS: Array<{ value: SurveyStatusCode; label: string }> = [
  { value: "draft", label: "Entwurf" },
  { value: "active", label: "Aktiv" },
  { value: "active_extended", label: "Aktiv (verlaengert)" },
  { value: "active_overdue", label: "Aktiv (ueberzogen)" },
  { value: "closed", label: "Beendet" },
  { value: "completed", label: "Abgeschlossen" },
  { value: "cancelled", label: "Abgebrochen" },
  { value: "archived", label: "Archiviert" },
];

const ALL_STATUS_CODES = STATUS_FILTERS.map(({ value }) => value);

export default function HomePage() {
  const [dataBundle, setDataBundle] = useState(createMockDataBundle);
  const [activeTab, setActiveTab] = useState<TabKey>("dashboard");
  const [selectedOrganizationId, setSelectedOrganizationId] = useState<number>(0);
  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const [flashMessage, setFlashMessage] = useState<string | null>(null);
  const [statusFilter, setStatusFilter] = useState<SurveyStatusCode[]>(() => [...ALL_STATUS_CODES]);

  const surveyViews = useMemo(() => buildSurveyViewModels(dataBundle), [dataBundle]);

  const organizations = useMemo(() => dataBundle.organizations, [dataBundle]);
  const bankOrganizations = useMemo(
    () => organizations.filter((organization) => organization.organizationCategoryId === 1),
    [organizations],
  );
  const organizationOptions = useMemo(
    () => [
      { value: "0", label: "Alle Organisationen" },
      ...bankOrganizations.map((organization) => ({
        value: organization.id.toString(),
        label: `${organization.shortName} (${organization.acnClientNumber})`,
      })),
    ],
    [bankOrganizations],
  );

  const selectedOrganizationOption = useMemo(
    () =>
      organizationOptions.find((option) => Number(option.value) === selectedOrganizationId) ?? organizationOptions[0],
    [organizationOptions, selectedOrganizationId],
  );

  const organizationSelectStyles = useMemo<StylesConfig<OrganizationOption, false>>(() => ({
    control: (provided, state) => ({
      ...provided,
      minHeight: 40,
      borderRadius: 12,
      borderColor: state.isFocused ? '#0ea5e9' : '#e2e8f0',
      boxShadow: 'none',
      '&:hover': { borderColor: '#0ea5e9' },
    }),
    valueContainer: (provided) => ({
      ...provided,
      padding: '0 12px',
    }),
    input: (provided) => ({
      ...provided,
      fontSize: '0.875rem',
      color: '#0f172a',
    }),
    placeholder: (provided) => ({
      ...provided,
      color: '#94a3b8',
    }),
    singleValue: (provided) => ({
      ...provided,
      fontSize: '0.875rem',
      color: '#0f172a',
    }),
    menu: (provided) => ({
      ...provided,
      borderRadius: 12,
      overflow: 'hidden',
    }),
    option: (provided, state) => ({
      ...provided,
      fontSize: '0.875rem',
      backgroundColor: state.isSelected
        ? '#0ea5e9'
        : state.isFocused
          ? 'rgba(14,165,233,0.1)'
          : 'white',
      color: state.isSelected ? 'white' : '#0f172a',
    }),
  }), []);

  const hpItUsers = useMemo(
    () => dataBundle.users.filter((user) => user.roleId === 1),
    [dataBundle.users],
  );

  const currentUser = useMemo(
    () => hpItUsers[0] ?? dataBundle.users[0],
    [hpItUsers, dataBundle.users],
  );

  const currentRole = useMemo(
    () => dataBundle.roles.find((role) => role.id === currentUser?.roleId),
    [dataBundle.roles, currentUser?.roleId],
  );

  useEffect(() => {
    if (selectedOrganizationId === 0) return;
    if (bankOrganizations.length === 0) return;
    const known = bankOrganizations.some((organization) => organization.id === selectedOrganizationId);
    if (!known) {
      setSelectedOrganizationId(bankOrganizations[0].id);
    }
  }, [bankOrganizations, selectedOrganizationId]);

  useEffect(() => {
    if (!flashMessage) return;
    const timeout = window.setTimeout(() => setFlashMessage(null), 4000);
    return () => window.clearTimeout(timeout);
  }, [flashMessage]);

  const openCreateForm = () => {
    setIsCreateOpen(true);
    setActiveTab("createSurvey");
  };

  const handleSelectTab = (tabKey: TabKey) => {
    setActiveTab(tabKey);
    setIsCreateOpen(tabKey === "createSurvey");
  };

  const resetStatusFilter = () => {
    setStatusFilter([...ALL_STATUS_CODES]);
  };

  const toggleStatusFilter = (value: SurveyStatusCode) => {
    setStatusFilter((current) => {
      if (current.includes(value)) {
        return current.filter((item) => item !== value);
      }
      return [...current, value];
    });
  };

  const drafts = useMemo(
    () => selectAndSort(surveyViews, (view) => view.survey.status === "draft"),
    [surveyViews],
  );

  const activeSurveys = useMemo(
    () => selectAndSort(surveyViews, (view) => ACTIVE_STATUSES.includes(view.survey.status)),
    [surveyViews],
  );

  const activeForOrganization = useMemo(() => {
    if (selectedOrganizationId === 0) return activeSurveys;
    return selectAndSort(activeSurveys, (view) => {
      const isRelevant = view.organizations.some((organization) => organization.id === selectedOrganizationId);
      return isRelevant && hasOutstandingResponse(view, selectedOrganizationId);
    });
  }, [activeSurveys, selectedOrganizationId]);

  const recent = useMemo(
    () => selectAndSort(surveyViews, (view) => RECENT_STATUSES.includes(view.survey.status)),
    [surveyViews],
  );

  const archive = useMemo(
    () => selectAndSort(surveyViews, (view) => view.survey.status === "archived"),
    [surveyViews],
  );

  const decisionMirror = useMemo(
    () =>
      selectAndSort(
        surveyViews,
        (view) =>
          DECISION_CATEGORY_CODES.has(view.category.code) &&
          (RECENT_STATUSES.includes(view.survey.status) || view.survey.status === "archived"),
      ),
    [surveyViews],
  );

  const tabData: Record<TabKey, SurveyViewModel[]> = {
    dashboard: surveyViews,
    // active: activeSurveys,
    "active-organization": activeForOrganization,
    drafts,
    recent,
    archive,
    decision: decisionMirror,
    createSurvey: [],
  };

  const tabMenuItems = TAB_ORDER.map((tabKey) => ({
    key: tabKey,
    label: TAB_LABELS[tabKey].title,
    count: tabData[tabKey].length,
    icon: TAB_ICONS[tabKey](),
  }));

  const handleCreateSurvey = (values: CreateSurveyFormValues) => {
    setDataBundle((previous) => {
      const nextSurveyId = previous.surveys.reduce((max, survey) => Math.max(max, survey.id), 0) + 1;
      const surveyNumber = generateSurveyNumber(previous.surveys);
      const today = new Date().toISOString().slice(0, 10);

      const newSurvey: Survey = {
        id: nextSurveyId,
        surveyNumber,
        name: values.name,
        shortDescription: values.shortDescription,
        longDescription: values.longDescription,
        status: "draft",
        responseDeadline: values.responseDeadline,
        createdById: values.createdById,
        topicId: values.topicId,
        surveyCategoryId: values.surveyCategoryId,
        decisionKeyId: values.decisionKeyId,
        totalAmountGross: values.totalAmountGross,
        personDays: values.personDays,
        dailyRateGross: values.dailyRateGross,
        recurringCostsGrossPa: values.recurringCostsGrossPa,
        createdAt: today,
      };

      return {
        ...previous,
        surveys: [...previous.surveys, newSurvey],
        surveyOrganizations: [
          ...previous.surveyOrganizations,
          ...values.organizationIds.map((organizationId) => ({
            surveyId: nextSurveyId,
            organizationId,
          })),
        ],
      };
    });

    setIsCreateOpen(false);
    setActiveTab("drafts");
    setFlashMessage(`Neue Umfrage "${values.name}" wurde als Entwurf angelegt.`);
  };

  if (!currentUser) {
    return null;
  }

  const isDashboard = activeTab === "dashboard";
  const baseItems = tabData[activeTab];

  const allStatusesSelected = statusFilter.length === STATUS_FILTERS.length;
  const filteredItems =
    // isDashboard
    //   ? []
    //   : activeTab === "active"
    //     ? baseItems.filter((item) => statusFilter.includes(item.survey.status))
    //     : baseItems;
    baseItems.filter((item) => statusFilter.includes(item.survey.status));

  return (
    <>
    <TopMenu user={currentUser} role={currentRole} onCreateSurvey={openCreateForm} />
      <div className="ml-80 min-h-screen bg-slate-50">
      <LeftMenu
        tabs={tabMenuItems}
        activeTab={activeTab}
        onSelectTab={handleSelectTab}
      />
        
        <main className="mx-auto flex w-full max-w-6xl flex-col gap-6 px-10 py-10">
          {flashMessage && (
            <div className="rounded-lg border border-emerald-200 bg-emerald-50 px-3 py-2 text-sm text-emerald-700">
              {flashMessage}
            </div>
          )}

          {isCreateOpen ? (
            <CreateSurveyForm
              categories={dataBundle.surveyCategories}
              topics={dataBundle.topics}
              organizations={dataBundle.organizations}
              users={hpItUsers.length > 0 ? hpItUsers : dataBundle.users}
              decisionKeys={dataBundle.decisionKeys}
              categoryDecisionKeys={dataBundle.categoryDecisionKeys}
              onSubmit={handleCreateSurvey}
              onCancel={() => setIsCreateOpen(false)}
            />
          ) : (
            <>
              <header className="space-y-3">
                <p className="max-w-3xl text-sm text-slate-600">
                  Die Anwendung bildet die aktualisierte Datenstruktur des HP-Forums ab: Organisationen, Rollen,
                  Topics, Surveys und Responses sind entsprechend des Fachkonzeptes modelliert. Diese Mock-Daten dienen
                  als Grundlage fuer die weitere Abstimmung von UX, Berechtigungen und Prozesslogik.
                </p>
                <p className="text-xs text-slate-500">
                  Stand der Datenbasis: {formatDate("2025-06-16")} | Quelle: Fachkonzept Version 1.0
                </p>
              </header>


              {/* {activeTab === "dashboard" && (
                <div className="mt-4 flex flex-wrap items-center gap-2 rounded-xl border border-slate-200 bg-white/80 p-3">
                  <button
                    type="button"
                    onClick={resetStatusFilter}
                    className={`rounded-full border px-3 py-1 text-xs font-medium transition-colors ${
                      allStatusesSelected
                        ? "border-sky-500 bg-sky-100 text-sky-700"
                        : "border-slate-200 bg-white text-slate-600 hover:border-sky-200 hover:text-sky-700"
                    }`}
                  >
                    Alle Status
                  </button>
                  {STATUS_FILTERS.map(({ value, label }) => {
                    const selected = statusFilter.includes(value);
                    return (
                      <button
                        key={value}
                        type="button"
                        onClick={() => toggleStatusFilter(value)}
                        className={`rounded-full border px-3 py-1 text-xs font-medium transition-colors ${
                          selected
                            ? "border-sky-500 bg-sky-500 text-white shadow-sm"
                            : "border-slate-200 bg-white text-slate-600 hover:border-sky-200 hover:text-sky-700"
                        }`}
                      >
                        {label}
                      </button>
                    );
                  })}
                </div>
              )} */}

              <section className="space-y-6">
                <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                  <div>
                    <h2 className="text-xl font-semibold text-slate-900">{TAB_LABELS[activeTab].title}</h2>
                    <p className="text-sm text-slate-600">{TAB_LABELS[activeTab].description}</p>
                  </div>

                  {['active-organization'].includes(activeTab) && (
                    <div className="flex flex-col gap-2 sm:flex-row sm:items-center sm:gap-3">
                      <label className="text-sm font-medium text-slate-700" htmlFor="organization-select">
                        Organisation
                      </label>
                      <div className="min-w-[16rem] sm:w-64">
                        <Select<OrganizationOption, false>
                          inputId="organization-select"
                          instanceId="organization-select"
                          classNamePrefix="hp-select"
                          options={organizationOptions}
                          value={selectedOrganizationOption}
                          onChange={(option) => setSelectedOrganizationId(Number(option?.value ?? 0))}
                          styles={organizationSelectStyles}
                          isSearchable
                          noOptionsMessage={() => 'Keine Treffer'}
                          placeholder="Organisation auswaehlen..."
                        />
                      </div>
                    </div>
                  )}
                </div>

                {isDashboard && (
                  <div>
                    <OverviewMetrics surveys={surveyViews} />

                    <div className="mt-4 flex flex-wrap items-center gap-2 rounded-xl border border-slate-200 bg-white/80 p-3">
                      <button
                        type="button"
                        onClick={resetStatusFilter}
                        className={`rounded-full border px-3 py-1 text-xs font-medium transition-colors cursor-pointer ${allStatusesSelected
                          ? "border-sky-500 bg-sky-100 text-sky-700"
                          : "border-slate-200 bg-white text-slate-600 hover:border-sky-200 hover:text-sky-700"
                          }`}
                      >
                        Alle Status
                      </button>
                      {STATUS_FILTERS.map(({ value, label }) => {
                        const selected = statusFilter.includes(value);
                        return (
                          <button
                            key={value}
                            type="button"
                            onClick={() => toggleStatusFilter(value)}
                            className={`rounded-full border px-3 py-1 text-xs font-medium transition-colors cursor-pointer ${selected
                              ? "border-sky-500 bg-sky-500 text-white shadow-sm"
                              : "border-slate-200 bg-white text-slate-600 hover:border-sky-200 hover:text-sky-700"
                              }`}
                          >
                            {label}
                          </button>
                        );
                      })}
                    </div>

                  </div>

                )}

                {activeTab === "decision" ? (
                  filteredItems.length > 0 ? (
                    <DecisionTable surveys={filteredItems} />
                  ) : (
                    <EmptyState activeTab={activeTab} />
                  )
                ) : filteredItems.length > 0 ? (
                  <SurveyAccordion
                    items={filteredItems}
                    focusOrganizationId={
                      activeTab === "active-organization" ? selectedOrganizationId : undefined
                    }
                  />
                ) : (
                  <EmptyState activeTab={activeTab} />
                )}
              </section>
            </>
          )}
        </main>
      </div>
    </>
  );
}

function selectAndSort(
  surveys: SurveyViewModel[],
  predicate: (view: SurveyViewModel) => boolean,
) {
  return surveys
    .filter(predicate)
    .slice()
    .sort((a, b) => {
      const dateA = new Date(a.survey.activationDate ?? a.survey.createdAt ?? 0).getTime();
      const dateB = new Date(b.survey.activationDate ?? b.survey.createdAt ?? 0).getTime();
      return dateB - dateA;
    });
}

function hasOutstandingResponse(view: SurveyViewModel, organizationId: number): boolean {
  const response = view.responses.find((item) => item.organization.id === organizationId);
  if (!response) return true;
  return response.responseStatus.code === "draft";
}

function EmptyState({ activeTab }: { activeTab: TabKey }) {
  const messageMap: Record<TabKey, string> = {
    dashboard: "Aktuell stehen keine Kennzahlen zur Verfuegung.",
    // active: "Aktuell sind keine aktiven Umfragen vorhanden.",
    "active-organization": "Fuer die ausgewaehlte Organisation liegen keine offenen Rueckmeldungen vor.",
    drafts: "Es liegen keine Entwuerfe vor.",
    recent: "In den letzten Tagen wurde keine Umfrage abgeschlossen.",
    archive: "Das Archiv ist leer.",
    decision: "Es liegen noch keine beschlossenen Umfragen vor.",
    createSurvey: "Bitte erstellen Sie eine neue Umfrage.",
  };

  return (
    <div className="rounded-2xl border border-dashed border-slate-300 bg-white p-10 text-center text-sm text-slate-500">
      {messageMap[activeTab]}
    </div>
  );
}




























