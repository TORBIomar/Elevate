import type { ReactNode } from 'react';

interface StatCardProps {
  icon: ReactNode;
  label: string;
  value: string | number;
  trend?: string;
  trendPositive?: boolean;
}

export default function StatCard({ icon, label, value, trend, trendPositive }: StatCardProps) {
  return (
    <div className="glass rounded-2xl p-6 card-hover">
      <div className="flex items-center gap-4">
        <div className="w-12 h-12 rounded-xl bg-primary-500/10 flex items-center justify-center text-primary-400">
          {icon}
        </div>
        <div>
          <p className="text-sm font-medium text-surface-400">{label}</p>
          <div className="flex items-baseline gap-2">
            <h3 className="text-2xl font-bold text-surface-100">{value}</h3>
            {trend && (
              <span className={`text-xs font-semibold ${trendPositive ? 'text-emerald-400' : 'text-red-400'}`}>
                {trend}
              </span>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
