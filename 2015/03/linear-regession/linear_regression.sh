#! octave-interpreter-name -qf
% Load csv file
data = load('total_by_period.csv');

% Define x and y
x = data(:,1);
y = data(:,2);

% Create a function to plot the data
function plotData(x,y)
plot(x,y,'rx','MarkerSize',8); % Plot the data
end

% Plot the data

plotData(x,y);

xlabel('Internet adoption in Colombia');
ylabel('Months since october 2011');

fprintf('Program paused. Press enter to continue.\n');

pause;

% Count how many data points we have
m = length(x); % Add a column of all ones (intercept term) to x
X = [ones(m, 1) x];

% Calculate theta

theta = (pinv(X'*X))*X'*y


% Plot the fitted equation we got from the regression

hold on; % this keeps our previous plot of the training data visible 
plot(X(:,2), X*theta, '-');
legend('Training data', 'Linear regression');
hold off % Do not put any more plots on this figure
pause;
