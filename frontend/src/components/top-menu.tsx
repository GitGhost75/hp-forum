import { Role, User } from "@/lib/types";

interface TopMenuProps {
  user: User;
  role: Role | undefined;
  onCreateSurvey: () => void;
}

export function TopMenu({ user, role, onCreateSurvey }: TopMenuProps) {
  const initials =
    user.name
      .split(" ")
      .filter(Boolean)
      .map((part) => part[0]?.toUpperCase())
      .slice(0, 2)
      .join("") || "HP";

  return (
    <header className="sticky top-0 z-30 flex h-16 items-center justify-between gap-6 border-b border-slate-200 bg-white/90 px-10 backdrop-blur">
      <div className="text-lg font-semibold uppercase tracking-wide text-sky-600">
        HP-Forum Prototyp
      </div>
      <div className="flex items-center gap-4">
        <button
          type="button"
          onClick={onCreateSurvey}
          className="hidden items-center gap-2 rounded-lg border border-sky-500 px-4 py-2 text-sm font-semibold text-sky-600 transition-colors hover:bg-sky-50 hover:text-sky-700 sm:flex"
        >
          Neue Umfrage
        </button>
        <div className="flex items-center gap-3">
          <div className="text-right">
            <p className="text-sm font-semibold text-slate-900">{user.name}</p>
            <p className="text-xs text-slate-500">{role?.name ?? "Rolle unbekannt"}</p>
          </div>
          <div className="flex h-10 w-10 items-center justify-center rounded-full bg-sky-100 text-sm font-semibold text-sky-700">
            {initials}
          </div>
        </div>
      </div>
    </header>
  );
}
