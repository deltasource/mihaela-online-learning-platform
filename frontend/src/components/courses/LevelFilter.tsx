import React from 'react';

interface LevelFilterProps {
  selectedLevel: string | null;
  onSelectLevel: (level: string | null) => void;
}

const LevelFilter: React.FC<LevelFilterProps> = ({ selectedLevel, onSelectLevel }) => {
  const levels = ['beginner', 'intermediate', 'advanced'];

  return (
    <div className="mb-8">
      <h3 className="text-lg font-semibold mb-3">Level</h3>
      <div className="flex flex-col space-y-2">
        <label className="inline-flex items-center">
          <input
            type="radio"
            className="form-radio h-4 w-4 text-blue-600"
            checked={selectedLevel === null}
            onChange={() => onSelectLevel(null)}
          />
          <span className="ml-2 text-gray-700">All Levels</span>
        </label>
        {levels.map((level) => (
          <label key={level} className="inline-flex items-center">
            <input
              type="radio"
              className="form-radio h-4 w-4 text-blue-600"
              checked={selectedLevel === level}
              onChange={() => onSelectLevel(level)}
            />
            <span className="ml-2 text-gray-700 capitalize">{level}</span>
          </label>
        ))}
      </div>
    </div>
  );
};

export default LevelFilter;