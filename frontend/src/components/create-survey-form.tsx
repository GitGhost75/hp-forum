"use client";

import { FormEvent, useMemo, useState } from "react";

import {
  CategoryDecisionKey,
  DecisionKey,
  Organization,
  SurveyCategory,
  Topic,
  User,
} from "@/lib/types";

export interface CreateSurveyFormValues {
  name: string;
  shortDescription: string;
  longDescription?: string;
  surveyCategoryId: number;
  topicId: number;
  createdById: number;
  responseDeadline: string;
  organizationIds: number[];
  decisionKeyId?: number;
  totalAmountGross?: number;
  personDays?: number;
  dailyRateGross?: number;
  recurringCostsGrossPa?: number;
}

interface CreateSurveyFormProps {
  categories: SurveyCategory[];
  topics: Topic[];
  organizations: Organization[];
  users: User[];
  decisionKeys: DecisionKey[];
  categoryDecisionKeys: CategoryDecisionKey[];
  onSubmit: (values: CreateSurveyFormValues) => void;
  onCancel: () => void;
}

export function CreateSurveyForm({
  categories,
  topics,
  organizations,
  users,
  decisionKeys,
  categoryDecisionKeys,
  onSubmit,
  onCancel,
}: CreateSurveyFormProps) {
  const [formState, setFormState] = useState({
    name: "",
    shortDescription: "",
    longDescription: "",
    surveyCategoryId: categories[0]?.id ?? 0,
    topicId: topics[0]?.id ?? 0,
    createdById: users[0]?.id ?? 0,
    responseDeadline: "",
    decisionKeyId: "",
    totalAmountGross: "",
    personDays: "",
    dailyRateGross: "",
    recurringCostsGrossPa: "",
  });

  const [selectedOrganizations, setSelectedOrganizations] = useState<Set<number>>(
    () => new Set(organizations.map((organization) => organization.id)),
  );
  const [error, setError] = useState<string | null>(null);

  const allowedDecisionKeys = useMemo(() => {
    const relations = categoryDecisionKeys.filter(
      (relation) => relation.surveyCategoryId === formState.surveyCategoryId,
    );

    if (relations.length === 0) {
      return decisionKeys;
    }

    const allowedIds = new Set(relations.map((relation) => relation.decisionKeyId));
    return decisionKeys.filter((key) => allowedIds.has(key.id));
  }, [categoryDecisionKeys, decisionKeys, formState.surveyCategoryId]);

  function handleToggleOrganization(organizationId: number) {
    setSelectedOrganizations((prev) => {
      const next = new Set(prev);
      if (next.has(organizationId)) {
        next.delete(organizationId);
      } else {
        next.add(organizationId);
      }
      return next;
    });
  }

  function parseNumberField(value: string): number | undefined {
    if (!value) return undefined;
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : undefined;
  }

  function parseIntegerField(value: string): number | undefined {
    if (!value) return undefined;
    const parsed = Number.parseInt(value, 10);
    return Number.isFinite(parsed) ? parsed : undefined;
  }

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!formState.name.trim()) {
      setError("Name der Umfrage ist erforderlich.");
      return;
    }

    if (!formState.shortDescription.trim()) {
      setError("Kurzbeschreibung ist erforderlich.");
      return;
    }

    if (!formState.responseDeadline) {
      setError("Rueckmeldefrist muss gesetzt werden.");
      return;
    }

    if (selectedOrganizations.size === 0) {
      setError("Mindestens eine Organisation muss ausgewahlt sein.");
      return;
    }

    setError(null);

    onSubmit({
      name: formState.name.trim(),
      shortDescription: formState.shortDescription.trim(),
      longDescription: formState.longDescription.trim() || undefined,
      surveyCategoryId: formState.surveyCategoryId,
      topicId: formState.topicId,
      createdById: formState.createdById,
      responseDeadline: formState.responseDeadline,
      organizationIds: Array.from(selectedOrganizations),
      decisionKeyId: formState.decisionKeyId ? Number(formState.decisionKeyId) : undefined,
      totalAmountGross: parseNumberField(formState.totalAmountGross),
      personDays: parseIntegerField(formState.personDays),
      dailyRateGross: parseNumberField(formState.dailyRateGross),
      recurringCostsGrossPa: parseNumberField(formState.recurringCostsGrossPa),
    });
  }

  return (
    <form className="space-y-6 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm" onSubmit={handleSubmit}>
      <header className="flex items-start justify-between gap-4">
        <div>
          <h2 className="text-xl font-semibold text-slate-900">Neue Umfrage erstellen</h2>
          <p className="mt-1 text-sm text-slate-600">
            Pflichtfelder sind minimal gehalten, Details koennen spaeter ergänzt werden. Standardstatus ist draft.
          </p>
        </div>
        <button
          type="button"
          onClick={onCancel}
          className="rounded-full border border-slate-200 px-3 py-1 text-sm text-slate-500 hover:border-slate-300 hover:text-slate-700"
        >
          Schliessen
        </button>
      </header>

      {error && (
        <div className="rounded-lg border border-rose-200 bg-rose-50 px-3 py-2 text-sm text-rose-700">
          {error}
        </div>
      )}

      <div className="grid gap-4 md:grid-cols-2">
        <label className="grid gap-1 text-sm">
          <span className="font-medium text-slate-700">Name *</span>
          <input
            type="text"
            value={formState.name}
            onChange={(event) => setFormState((prev) => ({ ...prev, name: event.target.value }))}
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
          />
        </label>
        <label className="grid gap-1 text-sm">
          <span className="font-medium text-slate-700">Kurzbeschreibung *</span>
          <input
            type="text"
            value={formState.shortDescription}
            onChange={(event) =>
              setFormState((prev) => ({ ...prev, shortDescription: event.target.value }))
            }
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
          />
        </label>
      </div>

      <label className="grid gap-1 text-sm">
        <span className="font-medium text-slate-700">Langbeschreibung</span>
        <textarea
          rows={4}
          value={formState.longDescription}
          onChange={(event) => setFormState((prev) => ({ ...prev, longDescription: event.target.value }))}
          className="rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
        />
      </label>

      <div className="grid gap-4 md:grid-cols-2">
        <label className="grid gap-1 text-sm">
          <span className="font-medium text-slate-700">Kategorie *</span>
          <select
            value={formState.surveyCategoryId}
            onChange={(event) =>
              setFormState((prev) => ({ ...prev, surveyCategoryId: Number(event.target.value) }))
            }
            className="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
          >
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name} ({category.code})
              </option>
            ))}
          </select>
        </label>
        <label className="grid gap-1 text-sm">
          <span className="font-medium text-slate-700">Thema *</span>
          <select
            value={formState.topicId}
            onChange={(event) => setFormState((prev) => ({ ...prev, topicId: Number(event.target.value) }))}
            className="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
          >
            {topics.map((topic) => (
              <option key={topic.id} value={topic.id}>
                {topic.name}
              </option>
            ))}
          </select>
        </label>
      </div>

      <div className="grid gap-4 md:grid-cols-2">
        <label className="grid gap-1 text-sm">
          <span className="font-medium text-slate-700">Verantwortlich (HP IT) *</span>
          <select
            value={formState.createdById}
            onChange={(event) =>
              setFormState((prev) => ({ ...prev, createdById: Number(event.target.value) }))
            }
            className="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
          >
            {users.map((user) => (
              <option key={user.id} value={user.id}>
                {user.name}
              </option>
            ))}
          </select>
        </label>
        <label className="grid gap-1 text-sm">
          <span className="font-medium text-slate-700">Rueckmeldefrist *</span>
          <input
            type="date"
            value={formState.responseDeadline}
            onChange={(event) =>
              setFormState((prev) => ({ ...prev, responseDeadline: event.target.value }))
            }
            className="rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
          />
        </label>
      </div>

      <div className="grid gap-4 md:grid-cols-2">
        <label className="grid gap-1 text-sm">
          <span className="font-medium text-slate-700">Entscheidungsschluessel</span>
          <select
            value={formState.decisionKeyId}
            onChange={(event) =>
              setFormState((prev) => ({ ...prev, decisionKeyId: event.target.value }))
            }
            className="rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
          >
            <option value="">- optional -</option>
            {allowedDecisionKeys.map((key) => (
              <option key={key.id} value={key.id}>
                {key.description} ({key.code})
              </option>
            ))}
          </select>
        </label>
        <div className="grid grid-cols-2 gap-4 text-sm">
          <label className="grid gap-1">
            <span className="font-medium text-slate-700">Gesamtbetrag brutto</span>
            <input
              type="number"
              min="0"
              step="0.01"
              value={formState.totalAmountGross}
              onChange={(event) =>
                setFormState((prev) => ({ ...prev, totalAmountGross: event.target.value }))
              }
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
            />
          </label>
          <label className="grid gap-1">
            <span className="font-medium text-slate-700">Personentage</span>
            <input
              type="number"
              min="0"
              step="1"
              value={formState.personDays}
              onChange={(event) =>
                setFormState((prev) => ({ ...prev, personDays: event.target.value }))
              }
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
            />
          </label>
          <label className="grid gap-1">
            <span className="font-medium text-slate-700">Tagsatz brutto</span>
            <input
              type="number"
              min="0"
              step="0.01"
              value={formState.dailyRateGross}
              onChange={(event) =>
                setFormState((prev) => ({ ...prev, dailyRateGross: event.target.value }))
              }
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
            />
          </label>
          <label className="grid gap-1">
            <span className="font-medium text-slate-700">Laufende Kosten p.a.</span>
            <input
              type="number"
              min="0"
              step="0.01"
              value={formState.recurringCostsGrossPa}
              onChange={(event) =>
                setFormState((prev) => ({ ...prev, recurringCostsGrossPa: event.target.value }))
              }
              className="rounded-lg border border-slate-300 px-3 py-2 text-sm text-slate-700 focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-200"
            />
          </label>
        </div>
      </div>

      <fieldset className="rounded-xl border border-slate-200 p-4">
        <legend className="px-2 text-sm font-semibold text-slate-700">
          Beteiligte Organisationen ({selectedOrganizations.size}/{organizations.length})
        </legend>
        <div className="mt-3 grid gap-2 md:grid-cols-2">
          {organizations.map((organization) => {
            const checked = selectedOrganizations.has(organization.id);
            return (
              <label
                key={organization.id}
                className="flex items-center gap-2 rounded-lg border border-slate-200 px-3 py-2 text-sm text-slate-700 hover:border-slate-300"
              >
                <input
                  type="checkbox"
                  checked={checked}
                  onChange={() => handleToggleOrganization(organization.id)}
                  className="h-4 w-4 rounded border-slate-300 text-sky-600 focus:ring-sky-200"
                />
                <span>
                  {organization.shortName} ({organization.acnClientNumber})
                </span>
              </label>
            );
          })}
        </div>
      </fieldset>

      <div className="flex justify-end gap-3">
        <button
          type="button"
          onClick={onCancel}
          className="rounded-lg border border-slate-200 px-4 py-2 text-sm font-medium text-slate-600 hover:border-slate-300 hover:text-slate-800"
        >
          Abbrechen
        </button>
        <button
          type="submit"
          className="rounded-lg bg-sky-600 px-4 py-2 text-sm font-semibold text-white shadow-sm hover:bg-sky-700"
        >
          Umfrage anlegen
        </button>
      </div>
    </form>
  );
}
