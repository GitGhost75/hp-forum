import { ReactNode } from "react";

interface LeftMenuTab<TTabKey extends string = string> {
  key: TTabKey;
  label: string;
  count: number;
  icon: ReactNode;
}

interface LeftMenuProps<TTabKey extends string = string> {
  tabs: LeftMenuTab<TTabKey>[];
  activeTab: TTabKey;
  onSelectTab: (tabKey: TTabKey) => void;
}

export function LeftMenu<TTabKey extends string = string>({
  tabs,
  activeTab,
  onSelectTab,
}: LeftMenuProps<TTabKey>) {
  return (
    <aside className="fixed inset-y-0 left-0 w-80 border-r border-slate-200 bg-white shadow-sm">
      <div className="flex h-full flex-col px-6 py-8 mt-8">
        <div>
          {/* <div className="text-lg font-semibold uppercase tracking-wide text-sky-600">
            HP-Forum Prototyp
          </div> */}
          <nav className="mt-8 space-y-1 text-sm text-slate-700" aria-label="Hauptnavigation">
            {tabs.map((tab) => {
              const isActive = tab.key === activeTab;
              return (
                <button
                  key={tab.key}
                  type="button"
                  onClick={() => onSelectTab(tab.key)}
                  className={`group flex w-full items-center justify-between rounded-lg px-3 py-2 text-left font-medium transition-colors ${
                    isActive
                      ? "bg-sky-500 text-white shadow-sm"
                      : "hover:bg-sky-50 hover:text-sky-700"
                  }`}
                >
                  <span className="flex items-center gap-2">
                    <span
                      className={`flex h-8 w-8 items-center justify-center text-sm ${
                        isActive ? "text-white" : "text-slate-500 group-hover:text-sky-600"
                      }`}
                      aria-hidden="true"
                    >
                      {tab.icon}
                    </span>
                    <span className="truncate">{tab.label}</span>
                  </span>
                  {/* {tab.key !== "dashboard" && tab.count > 0 && (
                    <span
                      className={`rounded-full px-2 py-0.5 text-xs font-semibold ${
                        isActive ? "bg-white/30" : "bg-slate-200 text-slate-600"
                      }`}
                    >
                      {tab.count}
                    </span>
                  )} */}
                </button>
              );
            })}
          </nav>
        </div>
      </div>
    </aside>
  );
}
