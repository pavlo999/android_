using FluentValidation;
using Sim23.Models;

namespace Sim23.Data.Validators
{
    public class ValidatorUserProfile : AbstractValidator<UserProfileViewModel>
    {
        public ValidatorUserProfile()
        {
            RuleFor(x => x.FirstName)
               .NotEmpty().WithMessage("Поле ім'я є обов'язковим!");

            RuleFor(x => x.LastName)
                .NotEmpty().WithMessage("Поле прізвище є обов'язковим!");
        }
    }
}
