
{{- define "calculator-chart.selectorLabels" -}}
app.kubernetes.io/name: {{  .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Chart name and version as used by the chart label
*/}}
{{- define "calculator-chart.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "calculator-chart.labels" -}}
helm.sh/chart: {{ include "calculator-chart.chart" . }}
{{ include "calculator-chart.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{- define "calculator-chart.serviceAccountName" -}}
{{- if .Values.serviceAccount.create -}}
    {{ default (printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-") .Values.serviceAccount.name }}
{{- else -}}
    {{ default "default" .Values.serviceAccount.name }}
{{- end -}}
{{- end -}}

{{/*
Names of user_api tier components
*/}}
{{- define "calculator-chart.user_api.defaultName" -}}
{{- printf "user_api-%s" .Release.Name -}}
{{- end -}}

{{- define "calculator-chart.user_api.deployment.name" -}}
{{- default (include "calculator-chart.user_api.defaultName" .) .Values.user_api.deployment.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.user_api.container.name" -}}
{{- default (include "calculator-chart.user_api.defaultName" .) .Values.user_api.container.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.user_api.service.name" -}}
{{- default (include "calculator-chart.user_api.defaultName" .) .Values.user_api.service.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.user_api.hpa.name" -}}
{{- default (include "calculator-chart.user_api.defaultName" .) .Values.user_api.hpa.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Names of card_api tier components
*/}}
{{- define "calculator-chart.card_api.defaultName" -}}
{{- printf "card_api-%s" .Release.Name -}}
{{- end -}}

{{- define "calculator-chart.card_api.deployment.name" -}}
{{- default (include "calculator-chart.card_api.defaultName" .) .Values.card_api.deployment.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.v.container.name" -}}
{{- default (include "calculator-chart.card_api.defaultName" .) .Values.card_api.container.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.card_api.service.name" -}}
{{- default (include "calculator-chart.card_api.defaultName" .) .Values.card_api.service.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.card_api.hpa.name" -}}
{{- default (include "calculator-chart.card_api.defaultName" .) .Values.card_api.hpa.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Names of gateway tier components
*/}}
{{- define "calculator-chart.gateway.defaultName" -}}
{{- printf "gateway-%s" .Release.Name -}}
{{- end -}}

{{- define "calculator-chart.gateway.deployment.name" -}}
{{- default (include "calculator-chart.gateway.defaultName" .) .Values.gateway.deployment.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.gateway.container.name" -}}
{{- default (include "calculator-chart.gateway.defaultName" .) .Values.gateway.container.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.gateway.service.name" -}}
{{- default (include "calculator-chart.gateway.defaultName" .) .Values.gateway.service.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.gateway.hpa.name" -}}
{{- default (include "calculator-chart.gateway.defaultName" .) .Values.gateway.hpa.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Annotation to update pods on Secrets or ConfigMaps updates
*/}}
{{- define "calculator-chart.propertiesHash" -}}
{{- $secrets := include (print $.Template.BasePath "/secrets.yaml") . | sha256sum -}}
{{- $urlConfig := include (print $.Template.BasePath "/urls-config.yaml") . | sha256sum -}}
{{ print $secrets $urlConfig | sha256sum }}
{{- end -}}

{{/*
Names of other components
*/}}
{{- define "calculator-chart.secrets.defaultName" -}}
{{- printf "secrets-%s" .Release.Name -}}
{{- end -}}

{{- define "calculator-chart.urlConfig.defaultName" -}}
{{- printf "url-config-%s" .Release.Name -}}
{{- end -}}

