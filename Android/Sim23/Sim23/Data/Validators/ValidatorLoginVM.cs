using FluentValidation;
using Sim23.Models;

namespace Sim23.Data.Validators
{
    public class ValidatorLoginVM : AbstractValidator<LoginViewModel>
    {
        public ValidatorLoginVM()
        {
            RuleFor(x => x.Email)
         .NotEmpty().WithMessage("Поле пошта є обов'язковим!")
         .EmailAddress().WithMessage("Пошта є не коректною!");

            RuleFor(x => x.Password)
              .NotEmpty().WithName("Password").WithMessage("Поле пароль є обов'язковим!")
              .MinimumLength(6).WithName("Password").WithMessage("Поле пароль має містити міннімум 6 символів!");
        }
    }
}
