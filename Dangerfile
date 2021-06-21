# Уведомляем разработчика если внесены изменения в конфигурационные файлы
configuration_files = [
  ".gitlab-ci.yml",
  "Dangerfile",
  "Gemfile",
  "Gemfile.lock",
  ".editorconfig",
  "detekt/detekt.yml",
  "detekt/baseline-debug.xml",
  "lint/lint.xml",
  "lint/baseline.xml",
]
configuration_files.each do |file|
  warn("Внесены изменения в конфигурационный файл #{gitlab.html_link(file)}.") if git.modified_files.include? file
end

# Проверяем, что для MR соблюдается флоу
branch = gitlab.branch_for_head
is_homework = branch =~ /^feature\/home-work-\d$/
is_bugfix = branch =~ /^bugfix\//
successful = File.exists?("build/reports/success")

unless is_homework || is_bugfix
  warn(
    ":thinking: Похоже, ветка названа не так как мы договаривались: #{branch}.\n"\
    "Если это ветка с домашним заданием используй формат `feature/home-work-{n}`, где `n` это номер домашки.\n"\
    "Для веток с фиксами по прошлым заданиям - `bugfix/...`\n"\
    "Пускай тут всё остаётся как есть, но будь внимательнее в следующий раз :wink:"
  )
end

if git.commits.any? { |c| c.message =~ /^Merge branch 'master'/ }
  warn("Вижу у тебя есть merge-коммиты из master. Хорошо бы поребейзить ветку перед тем как вливать - тогда история будет чище.")
end

if git.commits.any? { |c| c.message !~ /^(Merge branch)|\[.+\]/ }
  warn(
    ":oncoming_police_car: Виу-виу-виу! Нарушитель пойман на месте преступления.\n"\
    "Мы нашли сообщения коммитов не соответствующие формату `[Тэг] Описание`!"
  )
end

# if successful && is_homework && !gitlab.mr_json["assignee"]
if successful && is_homework && !gitlab.mr_json["assignee"]
  require "cgi"
  n = branch[-1]
  mr_link = CGI.escape(gitlab.mr_json["web_url"])
  mail_link = "mailto:?subject=#{CGI.escape("Internship homework ##{n} - ТЕМА ДЗ")}&body=#{mr_link}"
  markdown ["### :tada: Похоже, что задание готово!",
            "Все проверки прошли, критичных ошибок я не нашел, а значит можно [отправить письмо](#{mail_link}) "\
            "чтобы задание пришёл проверить более мощный робот.  \n"\
            "Адреса роботов и тема письма указаны в файле с домашним заданием."
           ]
end

# Если прошёл Detekt, парсим его отчёт
detekt_report = "build/reports/detekt.xml"
if File.exists?(detekt_report)
  checkstyle_format.base_path = Dir.pwd
  checkstyle_format.report(detekt_report)
end

# Парсим отчёты тестов, если они есть
junit_reports = Dir.glob("**/build/test-results/**/TEST-*.xml")
unless junit_reports.empty?
  junit.parse_files(junit_reports)
  junit.report
end

# Парсим отчёты линта, если они есть
lint_report = "build/reports/lint-results.xml"
if File.exists?(lint_report)
  android_lint.report_file = lint_report
  android_lint.skip_gradle_task = true
  android_lint.lint(inline_mode: true)
end

# Если на предыдущем этапе gradle сборка упала, то success файл будет отсутствовать
# В этом случае мы валим danger, MR нельзя мержить
unless successful
  pipeline_link = ENV["CI_PROJECT_URL"] + "/-/pipelines/" + ENV["CI_PIPELINE_ID"]
  fail("Сборка [упала](#{pipeline_link}). Проверь замечания или ошибки сборки.")
end
