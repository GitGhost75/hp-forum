import { getApprovalIndicatorColor, getResponseIndicatorColor } from "@/lib/utils";

interface IndicatorPillProps {
  type: "response" | "approval";
  numerator: number;
  denominator: number;
}

const labelMap: Record<IndicatorPillProps["type"], string> = {
  response: "Rücklauf",
  approval: "Zustimmung",
};

export function IndicatorPill({ type, numerator, denominator }: IndicatorPillProps) {
  const ratio = denominator === 0 ? 0 : numerator / denominator;
  const { bg, text } =
    type === "response"
      ? getResponseIndicatorColor(ratio)
      : getApprovalIndicatorColor(ratio);

  const formattedRatio = new Intl.NumberFormat("de-AT", {
    style: "percent",
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(ratio);

  return (
    <span
      className={`inline-flex items-center gap-1 rounded-full px-3 py-1 text-xs font-semibold ${bg} ${text}`}
    >
      <span className="inline-flex h-6 w-6 items-center justify-center rounded-full bg-white/80 text-[11px] font-bold text-slate-900">
        {numerator}/{denominator}
      </span>
      {labelMap[type]} {formattedRatio}
    </span>
  );
}
