import { SurveyCategory } from "@/lib/types";

const categoryColors: Record<string, string> = {
  NOM: "bg-sky-100 text-sky-700",
  CTB_PART: "bg-emerald-100 text-emerald-700",
  HPIT_DEC: "bg-rose-100 text-rose-700",
  GEN_DEC: "bg-indigo-100 text-indigo-700",
  ACN_INIT: "bg-amber-100 text-amber-700",
  GEN_SURV: "bg-slate-100 text-slate-700",
};

export function CategoryBadge({ category }: { category: SurveyCategory }) {
  const classes = categoryColors[category.code] ?? "bg-slate-100 text-slate-700";
  return (
    <span className={`inline-flex rounded-full px-3 py-1 text-xs font-medium ${classes}`}>
      {category.name}
    </span>
  );
}
